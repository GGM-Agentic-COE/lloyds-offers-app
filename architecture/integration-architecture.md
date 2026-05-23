# Integration Architecture — Smart Location-Based Offers Platform
**Version:** 1.0 · **Status:** Approved · **Last Updated:** May 2026

---

## 1. Integration Landscape

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        PLATFORM SERVICES (GKE)                               │
│                                                                             │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌────────────┐           │
│  │   auth     │  │   user     │  │   notif    │  │   offer    │           │
│  │  service   │  │  service   │  │  service   │  │  service   │           │
│  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘  └─────┬──────┘           │
│        │                │                │                │                 │
│  ┌─────┴────────────────┴────────────────┴────────────────┴──────────────┐  │
│  │                         Cloud Pub/Sub                                  │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│        │                │                │                │                 │
│  ┌─────▼──────┐  ┌──────▼─────┐  ┌──────▼─────┐  ┌──────▼─────┐          │
│  │  geofence  │  │  merchant  │  │  Cloud SQL │  │ Memorystore│          │
│  │  service   │  │  service   │  │ (Postgres) │  │  (Redis)   │          │
│  └─────┬──────┘  └─────┬──────┘  └────────────┘  └────────────┘          │
└────────┼────────────────┼────────────────────────────────────────────────────┘
         │                │
─────────┼────────────────┼──── EXTERNAL INTEGRATIONS ────────────────────────
         │                │
┌────────▼────┐  ┌────────▼────┐  ┌─────────────┐  ┌─────────────┐  ┌──────────┐
│    APNs     │  │    FCM      │  │ SMS Gateway │  │Email Service│  │Open Bank │
│  (Apple)    │  │  (Google)   │  │  (Twilio)   │  │ (SendGrid) │  │  (AISP)  │
└─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  └──────────┘
```

---

## 2. External Integrations

| # | System | Protocol | Direction | Service | Sprint | Purpose |
|---|---|---|---|---|---|---|
| INT-01 | APNs (Apple) | HTTP/2 REST | Outbound | notification-service | 1 | iOS push notifications |
| INT-02 | FCM (Google) | HTTP REST | Outbound | notification-service | 1 | Android push notifications |
| INT-03 | SMS Gateway (Twilio) | REST | Outbound | auth-service | 1 | OTP delivery via SMS |
| INT-04 | Email Service (SendGrid) | REST | Outbound | auth-service | 1 | OTP delivery via email |
| INT-05 | Cloud KMS | gRPC | Outbound | All services | 1 | CMEK encryption/decryption |
| INT-06 | Open Banking (AISP) | REST + OAuth2 redirect | Outbound | user-service | 3 | Transaction data sync |
| INT-07 | Cloud Scheduler | HTTP | Inbound | offer-service, geofence-service | 2 | Scheduled jobs (expiry, cleanup) |

### INT-01: Apple Push Notification Service (APNs)

| Attribute | Value |
|---|---|
| Endpoint | api.push.apple.com (prod), api.sandbox.push.apple.com (dev) |
| Protocol | HTTP/2 with TLS 1.3 |
| Auth | JWT (ES256 signed with Apple-issued key) |
| Payload | JSON, max 4KB |
| SLA | Best-effort delivery, no guaranteed latency |
| Rate Limit | None (Apple manages throttling) |
| Error Handling | 410 Gone = token invalid (remove device), 429 = throttled (backoff) |
| Retry | 3 attempts, exponential backoff (1s, 5s, 30s) |

### INT-02: Firebase Cloud Messaging (FCM)

| Attribute | Value |
|---|---|
| Endpoint | fcm.googleapis.com/v1/projects/{project}/messages:send |
| Protocol | HTTP/1.1 REST |
| Auth | OAuth2 service account (Google Application Default Credentials) |
| Payload | JSON, max 4KB |
| SLA | Best-effort delivery |
| Rate Limit | 600K messages/min per project |
| Error Handling | 404 = token invalid (remove), 429 = quota exceeded (backoff) |
| Retry | 3 attempts, exponential backoff (1s, 5s, 30s) |

### INT-03: SMS Gateway (Twilio)

| Attribute | Value |
|---|---|
| Endpoint | api.twilio.com/2010-04-01/Accounts/{sid}/Messages |
| Protocol | REST (HTTPS) |
| Auth | Basic Auth (Account SID + Auth Token) |
| Data Exchanged | To (phone), Body (OTP message), From (sender ID) |
| SLA | 99.95% uptime, delivery within 10s (domestic) |
| Rate Limit | 100 messages/second |
| Error Handling | 4xx = invalid request, 5xx = retry |
| Retry | 2 attempts, 5s backoff |
| Fallback | Switch to email delivery if SMS fails after retries |

### INT-04: Email Service (SendGrid)

| Attribute | Value |
|---|---|
| Endpoint | api.sendgrid.com/v3/mail/send |
| Protocol | REST (HTTPS) |
| Auth | Bearer API Key |
| Data Exchanged | To (email), Subject, HTML body (OTP template) |
| SLA | 99.95% uptime |
| Rate Limit | 600 emails/minute |
| Error Handling | 429 = rate limited, 5xx = retry |
| Retry | 2 attempts, 5s backoff |
| Fallback | Queue for delayed delivery |

### INT-05: Cloud KMS

| Attribute | Value |
|---|---|
| Protocol | gRPC (via client library) |
| Auth | Workload Identity (GKE pod → GCP IAM) |
| Purpose | Encrypt/decrypt sensitive fields, sign JWTs |
| SLA | 99.999% (Google SLA) |
| Latency | < 10ms per operation |
| Error Handling | Retry with exponential backoff |
| Key Rotation | Automatic, 90-day rotation |

### INT-06: Open Banking (AISP) — Sprint 3

| Attribute | Value |
|---|---|
| Protocol | REST + OAuth2 Authorization Code (redirect) |
| Auth | Client credentials + user consent redirect |
| Data Exchanged | Account transactions (merchant, amount, date, MCC code) |
| Sync Frequency | Daily batch + on-demand refresh |
| SLA | Provider-dependent (typically 99.5%) |
| Error Handling | Circuit breaker, graceful degradation to category-only matching |
| Retry | 3 attempts, exponential backoff |
| Compliance | PSD2 AISP registration required |

---

## 3. Internal Service Communication

### Synchronous (REST)

| Caller | Callee | Endpoint | Purpose | Timeout |
|---|---|---|---|---|
| geofence-service | offer-service | GET /api/v1/offers?zoneId={} | Get offers for matched zone | 2s |
| geofence-service | notification-service | POST /api/v1/notifications/send | Trigger push delivery | 2s |
| offer-service | notification-service | GET /api/v1/frequency-cap/check | Check if notification eligible | 500ms |
| offer-service | geofence-service | GET /api/v1/zones/{id} | Get zone details for offer | 1s |
| merchant-service | geofence-service | POST /api/v1/zones | Create zone for campaign | 5s |

### Headers (All Internal Calls)

| Header | Purpose | Required |
|---|---|---|
| X-Correlation-Id | Distributed tracing | Yes |
| X-Service-Auth | Service-to-service authentication | Yes |
| X-Request-Timeout | Remaining time budget (propagated) | Recommended |

---

## 4. Event-Driven Architecture (Pub/Sub)

### Topic Registry

| Topic | Publisher | Subscribers | Schema | Sprint |
|---|---|---|---|---|
| user.registered | auth-service | audit-service, analytics | UserRegisteredEvent | 1 |
| user.verified | auth-service | notification-service, audit | UserVerifiedEvent | 1 |
| user.deleted | user-service | All services | UserDeletedEvent | 3 |
| consent.recorded | auth-service | audit-service, analytics | ConsentRecordedEvent | 1 |
| consent.withdrawn | auth-service | user-service, offer-service | ConsentWithdrawnEvent | 3 |
| notification.requested | geofence-service, offer-service | notification-service | NotificationRequestedEvent | 1 |
| notification.delivered | notification-service | analytics, audit | NotificationDeliveredEvent | 1 |
| notification.failed | notification-service | analytics, alerting | NotificationFailedEvent | 1 |
| geofence.entry | geofence-service | offer-service | GeofenceEntryEvent | 2 |
| geofence.exit | geofence-service | analytics | GeofenceExitEvent | 2 |
| offer.redeemed | offer-service | merchant-service, analytics, notification | OfferRedeemedEvent | 2 |
| offer.expired | offer-service | notification-service, analytics | OfferExpiredEvent | 2 |
| campaign.published | merchant-service | geofence-service, offer-service | CampaignPublishedEvent | 3 |
| campaign.paused | merchant-service | geofence-service, offer-service | CampaignPausedEvent | 3 |

### Canonical Event Schema

```json
{
  "eventId": "uuid",
  "eventType": "domain.action",
  "timestamp": "ISO 8601",
  "correlationId": "uuid",
  "source": "service-name",
  "version": "1.0",
  "data": { }
}
```

### Dead Letter Configuration

| Topic | Max Delivery Attempts | Dead Letter Topic | Alert |
|---|---|---|---|
| All topics | 5 | {topic}.dead-letter | PagerDuty after 10 messages in DLQ |

---

## 5. Circuit Breaker Configuration

| Integration | Failure Threshold | Window | Break Duration | Fallback |
|---|---|---|---|---|
| APNs | 5 failures | 30s | 60s | Queue for retry on recovery |
| FCM | 5 failures | 30s | 60s | Queue for retry on recovery |
| SMS Gateway | 10 failures | 60s | 30s | Switch to email delivery |
| Email Service | 10 failures | 60s | 30s | Queue for delayed delivery |
| Open Banking | 3 failures | 60s | 120s | Skip personalization, use category-only |
| Cloud KMS | N/A | N/A | N/A | Fail request (cannot proceed without encryption) |
| Internal services | 5 failures | 30s | 30s | Return cached/default response |

### Circuit Breaker States

```
CLOSED ──[failures >= threshold]──▶ OPEN ──[break duration elapsed]──▶ HALF-OPEN
  ▲                                                                        │
  └────────────────────[success]───────────────────────────────────────────┘
  └────────────────────[failure]──▶ OPEN (reset break timer)
```

---

## 6. Retry Policies

| Integration | Max Retries | Backoff | Idempotent |
|---|---|---|---|
| APNs/FCM | 3 | Exponential (1s, 5s, 30s) | Yes (message ID) |
| SMS Gateway | 2 | Fixed 5s | Yes (Idempotency-Key) |
| Email Service | 2 | Fixed 5s | Yes (Idempotency-Key) |
| Open Banking | 3 | Exponential (2s, 10s, 60s) | Yes (request ID) |
| Internal REST | 2 | Fixed 1s | Yes (X-Correlation-Id) |
| Pub/Sub delivery | 5 | Exponential (10s, 30s, 60s, 120s, 300s) | Yes (eventId dedup) |

---

## 7. Geofence Event Processing Pipeline

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Device      │────▶│  geofence    │────▶│  Pub/Sub     │────▶│  offer       │
│  (entry evt) │     │  service     │     │  geofence.   │     │  service     │
│              │     │  (validate,  │     │  entry       │     │  (match,     │
│              │     │   city check)│     │              │     │   rank)      │
└──────────────┘     └──────────────┘     └──────────────┘     └──────┬───────┘
                                                                       │
                                                                       ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Consumer    │◀────│  APNs/FCM    │◀────│  notification│◀────│  Pub/Sub     │
│  (push)      │     │              │     │  service     │     │  notification│
│              │     │              │     │  (cap check, │     │  .requested  │
│              │     │              │     │   dispatch)  │     │              │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘

End-to-end latency budget: 2000ms
  Device → geofence-service:  200ms (network)
  geofence-service processing: 300ms (validate + city check + publish)
  Pub/Sub delivery:            100ms
  offer-service matching:      400ms (spatial query + preference filter + rank)
  Pub/Sub delivery:            100ms
  notification-service:        400ms (cap check + dispatch)
  APNs/FCM delivery:           500ms
```

---

## 8. Security for Integrations

| Integration | Credential Storage | Rotation | Access Control |
|---|---|---|---|
| APNs | Secret Manager (P8 key) | Annual (Apple-issued) | Workload Identity |
| FCM | Secret Manager (service account JSON) | 90 days | Workload Identity |
| SMS Gateway | Secret Manager (Account SID + Token) | 90 days | Workload Identity |
| Email Service | Secret Manager (API Key) | 90 days | Workload Identity |
| Open Banking | Secret Manager (client cert + secret) | Annual | Workload Identity |
| Cloud KMS | IAM (no stored credential) | N/A | Workload Identity |
| Internal services | Signed JWT (X-Service-Auth) | 24 hours (auto-rotate) | Service mesh mTLS |

---

## 9. Monitoring & Alerting for Integrations

| Integration | Metrics Tracked | Alert Threshold | Escalation |
|---|---|---|---|
| APNs | Delivery rate, error rate, latency | Error >5% for 5min | PagerDuty P2 |
| FCM | Delivery rate, error rate, latency | Error >5% for 5min | PagerDuty P2 |
| SMS Gateway | Delivery rate, cost per message | Delivery <90% for 10min | PagerDuty P1 |
| Email Service | Delivery rate, bounce rate | Bounce >10% | PagerDuty P2 |
| Open Banking | Sync success rate, latency | Failure >20% for 15min | PagerDuty P3 |
| Pub/Sub | DLQ depth, publish latency | DLQ >10 messages | PagerDuty P2 |
| Internal REST | Error rate, p95 latency | p95 >1s or error >1% | PagerDuty P2 |

---

## 10. Webhook Handling (Inbound Callbacks)

### Merchant POS Redemption Callback (Future)

For merchants with POS integration (post-MVP), the platform can receive redemption confirmations via webhook instead of the merchant app scanning QR codes.

| Attribute | Value |
|---|---|
| Endpoint | POST /api/v1/webhooks/redemption |
| Auth | HMAC-SHA256 signature in `X-Webhook-Signature` header |
| Payload | `{redemptionToken, merchantId, timestamp, amount}` |
| Verification | Compute HMAC of raw body with shared secret, compare to header |
| Idempotency | `redemptionToken` is unique — duplicate deliveries are safe (upsert) |
| Response | 200 OK (accepted), 401 (invalid signature), 400 (invalid payload) |
| Timeout | Merchant must receive response within 5s |
| Retry (merchant side) | Merchant retries 3x with exponential backoff if no 200 |

### Open Banking Consent Callback (Sprint 3)

| Attribute | Value |
|---|---|
| Endpoint | GET /api/v1/callbacks/openbanking |
| Purpose | OAuth2 redirect after user authorizes bank access |
| Auth | State parameter validation (CSRF protection) |
| Flow | Redirect → exchange code for token → store token → begin sync |
| Error | Invalid state → reject, expired code → prompt user to retry |

### Webhook Security Rules

1. **Always verify signatures** — never trust payload without HMAC validation
2. **Idempotent processing** — webhooks may be delivered multiple times
3. **Respond quickly** — return 200 immediately, process asynchronously via Pub/Sub
4. **Log all attempts** — including failed signature verifications (security audit)
5. **Rotate secrets** — webhook signing secrets rotated every 90 days via Secret Manager
