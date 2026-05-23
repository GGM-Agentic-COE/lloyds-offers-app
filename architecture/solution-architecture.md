# Solution Architecture — Smart Location-Based Offers Platform
**Version:** 1.0 · **Status:** Approved · **Last Updated:** May 2026

---

## 1. System Context

```
                              ┌─────────────────────┐
                              │    Consumer          │
                              │  (iOS / Android)     │
                              └──────────┬──────────┘
                                         │ HTTPS
                              ┌──────────▼──────────┐
                              │    API Gateway       │
                              │    (Apigee)          │
                              └──────────┬──────────┘
                                         │
          ┌──────────────────────────────┼──────────────────────────────┐
          │                              │                              │
┌─────────▼─────────┐     ┌─────────────▼──────────┐     ┌────────────▼───────────┐
│   auth-service    │     │    user-service         │     │  notification-service  │
└─────────┬─────────┘     └────────────────────────┘     └────────────┬───────────┘
          │                                                            │
          │                                                   ┌────────▼────────┐
          │                                                   │  APNs / FCM     │
          │                                                   └─────────────────┘
          │
┌─────────▼─────────┐     ┌────────────────────────┐     ┌────────────────────────┐
│  geofence-service │     │    offer-service        │     │   merchant-service     │
└─────────┬─────────┘     └────────────┬───────────┘     └────────────┬───────────┘
          │                            │                               │
          │                            │                    ┌──────────▼──────────┐
          │                            │                    │  Merchant Dashboard │
          │                            │                    │  (React Web App)    │
          │                            │                    └─────────────────────┘
          │                            │
┌─────────▼────────────────────────────▼───────────────────────────────────────────┐
│                         Google Cloud Platform                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────────────┐   │
│  │Cloud SQL │  │Memorystore│  │ Pub/Sub  │  │ BigQuery │  │ Cloud Scheduler  │   │
│  │(Postgres)│  │ (Redis)   │  │          │  │          │  │                  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────────────┘   │
└──────────────────────────────────────────────────────────────────────────────────┘

External Systems:
  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
  │ SMS Gateway  │  │ Email Service│  │ Open Banking │  │  Cloud KMS   │
  │ (Twilio)     │  │ (SendGrid)  │  │ (AISP)       │  │              │
  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘
```

### Actors

| Actor | Type | Interaction |
|---|---|---|
| Consumer | External (mobile app) | Registration, offers, redemption, history, settings |
| Merchant Marketer | External (web dashboard) | Registration, campaigns, analytics |
| Platform Operator | Internal (admin) | Merchant review, offer moderation, monitoring |
| System (Scheduler) | Internal | Offer expiry, analytics aggregation, data cleanup |

---

## 2. Bounded Contexts

| Context | Service | Responsibility | Key Entities |
|---|---|---|---|
| Identity & Access | auth-service | Registration, verification, sessions, consent | User, OtpCode, Session, Consent |
| User Profile | user-service | Preferences, profile, deletion, export | UserProfile, NotificationPreference |
| Notification Delivery | notification-service | Push dispatch, device management, frequency capping | DeviceToken, NotificationLog |
| Offer Management | offer-service | Offers, redemption, history, ratings, reporting | Offer, Redemption, Rating, Report |
| Geospatial | geofence-service | Zones, event processing, city boundaries | GeofenceZone, GeofenceEvent, City |
| Merchant Operations | merchant-service | Registration, campaigns, analytics | Merchant, Campaign, AnalyticsEvent |

---

## 3. Technology Decisions

| Layer | Technology | Rationale |
|---|---|---|
| Mobile (iOS) | Swift 5.9+ | Native geofencing (CLLocationManager), biometric (LocalAuthentication), battery optimization |
| Mobile (Android) | Kotlin 1.9+ | Native geofencing (GeofencingClient), biometric (BiometricPrompt) |
| Merchant Web | React (TypeScript) | Lloyds standard for internal/colleague-facing web apps |
| Backend | Java 21, Spring Boot 3.x | Lloyds standard, GKE-native, ecosystem (Resilience4j, Spring Security) |
| Container | Docker on GKE | Lloyds standard container orchestration |
| Database | Cloud SQL (PostgreSQL 15) | ACID, PostGIS for spatial queries, managed HA |
| Cache | Memorystore (Redis 7) | Sub-ms reads for frequency capping within 2s SLA |
| Messaging | Cloud Pub/Sub | Guaranteed delivery, dead-letter, multi-subscriber |
| Analytics | BigQuery | Merchant analytics, spending pattern analysis |
| Stream Processing | Dataflow (Apache Beam) | Transaction analysis pipeline |
| Scheduling | Cloud Scheduler | Offer expiry, data cleanup, analytics aggregation |
| API Gateway | Apigee | Rate limiting, auth validation, routing |
| CDN | Cloud CDN | Offer images, static assets |
| Secrets | Secret Manager | API keys, signing keys, encryption keys |
| Encryption | Cloud KMS (CMEK) | Customer-managed keys for data at rest |
| Observability | Cloud Monitoring + Logging + Trace | Metrics, structured logs, distributed tracing |
| CI/CD | Cloud Build → Cloud Deploy | Automated build, test, deploy pipeline |
| IaC | Terraform | Infrastructure provisioning and management |

---

## 4. Security Architecture

### Authentication Flows

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Registration│────▶│  OTP Verify  │────▶│  JWT Issued  │
│  (phone/email)     │  (6-digit)   │     │  (access +   │
└──────────────┘     └──────────────┘     │   refresh)   │
                                          └──────┬───────┘
                                                 │
                          ┌──────────────────────┼──────────────────────┐
                          │                      │                      │
                   ┌──────▼──────┐      ┌────────▼────────┐    ┌───────▼───────┐
                   │  Biometric  │      │  Password Login │    │ Token Refresh │
                   │  (Face ID)  │      │  (fallback)     │    │ (silent)      │
                   └─────────────┘      └─────────────────┘    └───────────────┘
```

### Encryption

| Data | At Rest | In Transit | Key Management |
|---|---|---|---|
| User PII (phone, email) | AES-256 (CMEK) | TLS 1.3 | Cloud KMS |
| OTP codes | SHA-256 hash (one-way) | TLS 1.3 | N/A (hashed) |
| Refresh tokens | SHA-256 hash | TLS 1.3 | N/A (hashed) |
| Location data | AES-256 (CMEK) | TLS 1.3 | Cloud KMS |
| Transaction data | AES-256 (CMEK) | TLS 1.3 | Cloud KMS |
| Offer images | Standard GCS encryption | TLS 1.3 + CDN | Google-managed |

### Rate Limiting

| Endpoint Category | Limit | Window | Action on Exceed |
|---|---|---|---|
| Registration | 5 per device | 1 hour | 429 + Retry-After |
| OTP requests | 3 per user | 10 minutes | 429 + Retry-After |
| OTP attempts | 5 per code | Lifetime | 30-min lockout |
| Read APIs | 100 per user | 1 minute | 429 + Retry-After |
| Write APIs | 20 per user | 1 minute | 429 + Retry-After |
| Merchant APIs | 50 per merchant | 1 minute | 429 + Retry-After |

---

## 5. Deployment Model

### GKE Cluster Configuration

```
Region: europe-west2 (London)
Zones: 3 (a, b, c) — multi-zone for HA
Node pools:
  - default: e2-standard-4 (4 vCPU, 16GB) — general workloads
  - high-memory: e2-highmem-4 — geofence matching (spatial queries)
```

### Service Deployment

| Service | Min Replicas | Max Replicas | HPA Metric | PDB |
|---|---|---|---|---|
| auth-service | 3 | 15 | CPU 60% | minAvailable: 2 |
| user-service | 2 | 10 | CPU 60% | minAvailable: 1 |
| notification-service | 2 | 30 | Queue depth | minAvailable: 2 |
| offer-service | 3 | 20 | Request rate | minAvailable: 2 |
| geofence-service | 3 | 50 | Event queue + CPU | minAvailable: 2 |
| merchant-service | 2 | 10 | CPU 60% | minAvailable: 1 |
| api-gateway (Apigee) | Managed | Managed | — | — |

### Disaster Recovery

| Component | RPO | RTO | Strategy |
|---|---|---|---|
| Cloud SQL | 0 (synchronous) | < 60s | Regional HA with auto-failover |
| Redis | < 1s | < 30s | HA with replica promotion |
| Pub/Sub | 0 | 0 | Multi-zone by default |
| GKE | 0 | < 5min | Multi-zone, pod rescheduling |
| BigQuery | 0 | 0 | Multi-region by default |

---

## 6. Data Architecture

### Per-Service Data Ownership

| Service | Primary Store | Tables | Sensitive Data |
|---|---|---|---|
| auth-service | Cloud SQL | users, otp_codes, user_sessions, consents, audit_log | PII (phone/email), tokens |
| user-service | Cloud SQL | user_profiles, notification_preferences | Spending preferences |
| notification-service | Cloud SQL + Redis | device_tokens, notification_log | Push tokens |
| offer-service | Cloud SQL | offers, redemptions, ratings, reports, share_links | Redemption history |
| geofence-service | Cloud SQL (PostGIS) | geofence_zones, geofence_events, cities | Location events |
| merchant-service | Cloud SQL + BigQuery | merchants, campaigns, analytics_events | Business data |

### Data Retention

| Data Type | Retention | Basis |
|---|---|---|
| User account data | Until deletion requested | GDPR Art. 5(1)(e) |
| Consent records | 7 years post-account closure | Regulatory audit |
| OTP codes | 24 hours | Security (auto-purge) |
| Geofence events | 24 hours | Data minimization (NFR-12) |
| Notification log | 90 days | Operational debugging |
| Redemption history | Until deletion requested | User value |
| Analytics (aggregated) | Indefinite | Anonymized, no PII |
| Audit log | 7 years | Regulatory compliance |

### Database Migration Strategy

- **Tool**: Flyway (integrated into Spring Boot startup)
- **Approach**: Forward-only migrations (no rollback scripts — deploy fix-forward)
- **Naming**: `V{sprint}_{sequence}__{description}.sql` (e.g., `V1_001__create_users_table.sql`)
- **Zero-downtime rules**:
  - New columns must be nullable or have defaults
  - Never rename or drop columns in the same release as code changes
  - Use expand-contract pattern: add new → migrate data → remove old (across 2 releases)
- **Execution**: Migrations run automatically on pod startup (leader election via advisory lock)
- **Testing**: All migrations tested against production-clone schema in CI pipeline

---

## 7. Cross-Cutting Concerns

### Observability

| Concern | Tool | Implementation |
|---|---|---|
| Metrics | Cloud Monitoring + Prometheus | Custom metrics per service (request rate, latency, error rate) |
| Logging | Cloud Logging | Structured JSON, correlation IDs, no PII in logs |
| Tracing | Cloud Trace | End-to-end request tracing across services |
| Alerting | Cloud Monitoring → PagerDuty | Critical: error >1%, latency p95 >2s |
| Dashboards | Cloud Monitoring | Per-service + business metrics (redemption rate) |

### Audit Logging

Every service logs to its own `audit_log` table:
- **What**: action, resource_type, resource_id
- **Who**: user_id, device_id, ip_address
- **When**: timestamp (UTC)
- **Context**: correlation_id, metadata (JSON)
- **Never logged**: passwords, OTP values, full tokens, raw location coordinates

### Feature Flags

Not implemented in Sprint 1. Planned for Sprint 2+ using a lightweight config-based approach (environment variables per service, refreshed via ConfigMap).

---

## 8. Architecture Decision Records

| ADR | Decision | Status | Sprint |
|---|---|---|---|
| ADR-01 | Native mobile (Swift + Kotlin) | Accepted | 1 |
| ADR-02 | Spring Boot on GKE | Accepted | 1 |
| ADR-03 | Cloud SQL (PostgreSQL) with PostGIS | Accepted | 1 |
| ADR-04 | Redis for rate limiting/frequency capping | Accepted | 1 |
| ADR-05 | JWT with refresh token rotation | Accepted | 1 |
| ADR-06 | Append-only consent records | Accepted | 1 |
| ADR-07 | Pub/Sub for async events | Accepted | 1 |
| ADR-08 | OTP stored as SHA-256 hash | Accepted | 1 |
| ADR-09 | Separate microservice per bounded context | Accepted | 1 |
| ADR-10 | Apigee as API gateway | Accepted | 1 |
| ADR-11 | PostGIS for spatial queries (geofencing) | Accepted | 2 |
| ADR-12 | BigQuery for merchant analytics | Accepted | 3 |
| ADR-13 | Dataflow for transaction analysis pipeline | Accepted | 3 |
| ADR-14 | Cloud CDN for offer images | Accepted | 2 |
