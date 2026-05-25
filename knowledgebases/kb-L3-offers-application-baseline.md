# Application Baseline — Smart Location-Based Offers Platform
### kb-L3-offers-application-baseline v1.0.0
### Current state after Sprint 1 implementation. Used by agents to classify requirements as new/enhancement/existing.

---

## BL1: Product Overview

| Attribute | Value |
|---|---|
| Product Name | Smart Location-Based Offers Platform |
| Platform | Native Mobile (iOS + Android) + Merchant Web Dashboard |
| Cloud Provider | Google Cloud Platform (GCP) |
| Primary Language (Backend) | Java 21 (Spring Boot 3.x) |
| Primary Language (iOS) | Swift 5.9+ |
| Primary Language (Android) | Kotlin 1.9+ |
| Primary Language (Merchant Web) | React (TypeScript) |
| Container Orchestration | Google Kubernetes Engine (GKE) |
| Database | Cloud SQL (PostgreSQL 15) |
| Cache | Memorystore (Redis 7) |
| Messaging | Google Cloud Pub/Sub |
| CI/CD | Cloud Build → Cloud Deploy → GKE |
| IaC | Terraform |
| Observability | Cloud Monitoring, Cloud Logging, Cloud Trace |

---

## BL2: Feature Inventory (Sprint 1)

| Feature ID | Feature Name | Status | Module | Description |
|---|---|---|---|---|
| F-00.1 | Design Token System & Component Library | ✅ Sprint 1 | UI Foundation | Colors, typography, spacing, reusable components (Button, TextInput, Card, Badge, Toast) |
| F-00.2 | App Shell & Tab Navigation | ✅ Sprint 1 | UI Foundation | 4-tab bottom navigation (Offers, History, Profile, Settings), deep linking, error/loading states |
| F-01.1 | Phone/Email Registration | ✅ Sprint 1 | Auth | Account creation with E.164/RFC 5322 validation, duplicate check, rate limiting |
| F-01.2 | OTP Verification | ✅ Sprint 1 | Auth | 6-digit OTP, 90s expiry, 3/10min rate limit, 5-attempt lockout |
| F-01.3 | Biometric Authentication Enrollment | ✅ Sprint 1 | Auth | Face ID/Touch ID enrollment, secure enclave token storage, fallback to PIN |
| F-01.4 | Consent Collection Flow | ✅ Sprint 1 | Privacy | Granular consent for location (always/app-open/off) and transaction data (allow/deny) |
| F-01.5 | Session Management & Token Refresh | ✅ Sprint 1 | Auth | JWT access (15min) + refresh (30d) with rotation, multi-device support |
| F-02.1 | Push Notification Service Integration | ✅ Sprint 1 | Notifications | APNs + FCM integration, device token management, retry logic, batch sending |
| F-02.2 | Notification Preference Controls | ✅ Sprint 1 | Notifications | Mode (always/app-open/off), quiet hours, category toggles |
| F-02.3 | Notification Frequency Capping | ✅ Sprint 1 | Notifications | 1 per offer/day, 5 total/day, 30-min gap, Redis-backed |
| F-02.4 | User Profile & Spending Preferences | ✅ Sprint 1 | Profile | Display name, spending categories, budget sensitivity, preferred radius |
| F-02.5 | Push Permission Request & Fallback | ✅ Sprint 1 | Notifications | Pre-permission screen, denial handling, in-app-only fallback mode |

---

## BL3: Screen Inventory (Mobile)

| Screen | Route | Features Used | Status |
|---|---|---|---|
| Registration | /onboarding/register | F-01.1 | ✅ Sprint 1 |
| OTP Verification | /onboarding/verify | F-01.2 | ✅ Sprint 1 |
| Biometric Enrollment | /onboarding/biometric | F-01.3 | ✅ Sprint 1 |
| Location Consent | /onboarding/consent-location | F-01.4 | ✅ Sprint 1 |
| Transaction Consent | /onboarding/consent-transaction | F-01.4 | ✅ Sprint 1 |
| Push Permission | /onboarding/push-permission | F-02.5 | ✅ Sprint 1 |
| Offers Tab (placeholder) | /offers | F-00.2 | ✅ Sprint 1 |
| History Tab (placeholder) | /history | F-00.2 | ✅ Sprint 1 |
| Profile Tab | /profile | F-02.4 | ✅ Sprint 1 |
| Settings Tab | /settings | F-02.2 | ✅ Sprint 1 |
| Notification Preferences | /settings/notifications | F-02.2 | ✅ Sprint 1 |

---

## BL4: API Inventory (All Sprints)

**Spec Location:** `api-specs/` (one OpenAPI 3.0.3 YAML per service)

| Service | Spec File | Endpoints | Sprint | Auth |
|---|---|---|---|---|
| auth-service | auth-service.yaml | 9 | 1 | Public (register/OTP/refresh) + JWT |
| user-service | user-service.yaml | 11 | 1 | JWT |
| notification-service | notification-service.yaml | 8 | 1 | JWT + X-Service-Auth (internal) |
| offer-service | offer-service.yaml | 10 | 2 | JWT + Merchant Auth |
| geofence-service | geofence-service.yaml | 10 | 2 | Merchant Auth + User JWT + Public |
| merchant-service | merchant-service.yaml | 13 | 3 | Merchant JWT |

### Endpoint Summary

| Service | Endpoint | Method | Purpose |
|---|---|---|---|
| **auth** | /api/v1/auth/register | POST | Create account |
| **auth** | /api/v1/auth/verify-otp | POST | Verify OTP |
| **auth** | /api/v1/auth/resend-otp | POST | Resend OTP |
| **auth** | /api/v1/auth/refresh | POST | Refresh tokens |
| **auth** | /api/v1/auth/logout | POST | Invalidate session |
| **auth** | /api/v1/auth/consent | POST | Record consent |
| **auth** | /api/v1/auth/consent | GET | Get consent state |
| **user** | /api/v1/users/me | GET | Get user info |
| **user** | /api/v1/users/me | PATCH | Update user |
| **user** | /api/v1/users/me | DELETE | Request deletion |
| **user** | /api/v1/users/me/biometric | PATCH | Update biometric pref |
| **user** | /api/v1/users/me/profile | GET | Get preferences |
| **user** | /api/v1/users/me/profile | PATCH | Update preferences |
| **user** | /api/v1/users/me/preferences/notifications | GET | Get notification prefs |
| **user** | /api/v1/users/me/preferences/notifications | PATCH | Update notification prefs |
| **user** | /api/v1/users/me/export | GET | Request data export |
| **notification** | /api/v1/devices | POST | Register push token |
| **notification** | /api/v1/devices/{deviceId} | DELETE | Deregister device |
| **notification** | /api/v1/notifications/send | POST | Send notification (internal) |
| **notification** | /api/v1/notifications/history | GET | Notification history |
| **notification** | /api/v1/notifications/{id}/read | POST | Mark as read |
| **notification** | /api/v1/frequency-cap/check | GET | Check frequency cap (internal) |
| **offer** | /api/v1/offers | GET | Nearby offers |
| **offer** | /api/v1/offers/{offerId} | GET | Offer detail |
| **offer** | /api/v1/offers/{offerId}/redeem | POST | Generate QR token |
| **offer** | /api/v1/offers/{offerId}/confirm-redemption | POST | Merchant confirms |
| **offer** | /api/v1/offers/history | GET | Redemption history |
| **offer** | /api/v1/offers/{offerId}/report | POST | Report offer |
| **offer** | /api/v1/offers/{offerId}/rate | POST | Rate offer |
| **offer** | /api/v1/offers/{offerId}/share | POST | Generate share link |
| **geofence** | /api/v1/zones | POST | Create zone |
| **geofence** | /api/v1/zones | GET | List zones |
| **geofence** | /api/v1/zones/{zoneId} | GET/PUT/DELETE | Zone CRUD |
| **geofence** | /api/v1/events | POST | Geofence event |
| **geofence** | /api/v1/cities | GET | Supported cities |
| **geofence** | /api/v1/cities/{cityId}/notify-interest | POST | City launch interest |
| **merchant** | /api/v1/merchants/register | POST | Business registration |
| **merchant** | /api/v1/merchants/me | GET | Merchant profile |
| **merchant** | /api/v1/campaigns | POST | Create campaign |
| **merchant** | /api/v1/campaigns | GET | List campaigns |
| **merchant** | /api/v1/campaigns/{id} | GET/PUT | Campaign CRUD |
| **merchant** | /api/v1/campaigns/{id}/publish | POST | Publish campaign |
| **merchant** | /api/v1/campaigns/{id}/pause | POST | Pause campaign |
| **merchant** | /api/v1/analytics/overview | GET | Analytics overview |
| **merchant** | /api/v1/analytics/campaigns/{id} | GET | Campaign analytics |
| **merchant** | /api/v1/analytics/export | GET | Export CSV |

---

## BL5: Data Model (Tables)

| Table | Key Columns | PII/Sensitive | Encryption | Status |
|---|---|---|---|---|
| users | id, identifier, identifier_type, status, display_name, biometric_enabled, created_at, verified_at | identifier (phone/email) | AES-256 at rest (CMEK) | ✅ Sprint 1 |
| otp_codes | id, user_id, code_hash, expires_at, used, attempts, created_at | code_hash (SHA-256) | Column-level (hash only) | ✅ Sprint 1 |
| user_sessions | id, user_id, device_id, device_name, refresh_token_hash, expires_at, last_active_at | refresh_token_hash | AES-256 at rest | ✅ Sprint 1 |
| consents | id, user_id, consent_type, decision, consent_version, recorded_at, superseded_at | — | AES-256 at rest | ✅ Sprint 1 |
| user_profiles | id, user_id, categories, budget_sensitivity, preferred_radius_meters, updated_at | categories (spending behavior) | AES-256 at rest | ✅ Sprint 1 |
| notification_preferences | id, user_id, mode, quiet_hours_start, quiet_hours_end, categories, updated_at | — | AES-256 at rest | ✅ Sprint 1 |
| device_tokens | id, user_id, device_id, platform, push_token, is_active, registered_at, last_used_at | push_token | AES-256 at rest | ✅ Sprint 1 |
| notification_log | id, user_id, notification_type, offer_id, status, sent_at, delivered_at | — | AES-256 at rest | ✅ Sprint 1 |
| audit_log | id, user_id, action, resource_type, resource_id, metadata, ip_address, device_id, created_at | ip_address | AES-256 at rest (append-only) | ✅ Sprint 1 |

---

## BL6: Integration Inventory

| System | Protocol | Purpose | Circuit Breaker | Status |
|---|---|---|---|---|
| APNs (Apple) | HTTP/2 REST | iOS push notifications | 5 failures/30s → 60s break | ✅ Sprint 1 |
| FCM (Google) | HTTP REST | Android push notifications | 5 failures/30s → 60s break | ✅ Sprint 1 |
| SMS Gateway | REST | OTP delivery via SMS | 10 failures/60s → 30s break | ✅ Sprint 1 |
| Email Service | REST | OTP delivery via email | 10 failures/60s → 30s break | ✅ Sprint 1 |
| Redis (Memorystore) | Redis protocol | Rate limiting, frequency capping, session cache | HA with auto-failover | ✅ Sprint 1 |
| Cloud KMS | gRPC | Customer-managed encryption keys (CMEK) for data at rest | Managed (no CB needed) | ✅ Sprint 1 |

---

## BL7: Event Topics (Pub/Sub)

| Topic | Publisher | Subscribers | Purpose | Status |
|---|---|---|---|---|
| user.registered | Auth Service | Audit Service, Analytics | New account created | ✅ Sprint 1 |
| user.verified | Auth Service | Notification Service, Audit | Account verified via OTP | ✅ Sprint 1 |
| consent.recorded | Auth Service | Audit Service, Analytics | Consent decision captured | ✅ Sprint 1 |
| notification.requested | Any Service | Notification Service | Request to send push notification | ✅ Sprint 1 |
| notification.delivered | Notification Service | Analytics, Audit | Push successfully delivered | ✅ Sprint 1 |
| notification.failed | Notification Service | Analytics, Alerting | Push delivery failed | ✅ Sprint 1 |

---

## BL8: Security Architecture

| Layer | Implementation | Status |
|---|---|---|
| Network | VPC, Private Service Connect, Cloud Armor (WAF/DDoS) | ✅ Sprint 1 |
| Identity | Cloud IAM, OAuth 2.0 (JWT), Workload Identity Federation | ✅ Sprint 1 |
| Data at Rest | AES-256 via Cloud KMS (CMEK) | ✅ Sprint 1 |
| Data in Transit | TLS 1.3 (all service-to-service and client-to-server) | ✅ Sprint 1 |
| Authentication | JWT access token (15min) + refresh rotation (30d) + biometric | ✅ Sprint 1 |
| Rate Limiting | 5 registrations/device/hour, 3 OTPs/10min, 100 req/s/user | ✅ Sprint 1 |
| Secrets | Secret Manager (API keys, signing keys) | ✅ Sprint 1 |
| Audit | Append-only audit_log table, all auth events logged | ✅ Sprint 1 |

---

## BL9: Service Architecture

| Service | Responsibility | Sprint | Replicas | Data Store |
|---|---|---|---|---|
| auth-service | Registration, OTP, session, consent | 1 | 3 (min) | Cloud SQL (users, otp_codes, sessions, consents) |
| user-service | Profile, preferences, deletion, export | 1 | 2 (min) | Cloud SQL (user_profiles, notification_preferences) |
| notification-service | Push dispatch, device tokens, frequency cap | 1 | 2 (min) | Cloud SQL (device_tokens, notification_log) + Redis |
| offer-service | Offers, redemptions, history, ratings, sharing | 2 | 3 (min) | Cloud SQL (offers, redemptions, ratings, reports) |
| geofence-service | Zone management, event processing, city boundaries | 2 | 3 (min) | Cloud SQL + PostGIS (geofence_zones, cities) |
| merchant-service | Registration, campaigns, analytics | 3 | 2 (min) | Cloud SQL (merchants, campaigns) + BigQuery (analytics) |
| api-gateway | Request routing, auth validation, rate limiting | 1 | 3 (min) | — (stateless, Apigee) |

---

## BL10: Architecture Decisions (ADRs)

**Full documentation:**
- `architecture/solution-architecture.md` — System context, bounded contexts, security, deployment, data architecture
- `architecture/integration-architecture.md` — External integrations, Pub/Sub events, circuit breakers, retry policies
- `architecture/lld.md` — Database schemas (19 tables), CQRS handlers (38 total), state machines (5), scheduled jobs (7)

| ADR | Decision | Rationale |
|---|---|---|
| ADR-01 | Native mobile (Swift + Kotlin) | Geofencing, biometric, battery optimization require native OS APIs |
| ADR-02 | Spring Boot on GKE | Lloyds standard, proven scalability, ecosystem support |
| ADR-03 | Cloud SQL (PostgreSQL) | ACID transactions, PostGIS for Sprint 2, managed HA |
| ADR-04 | Redis for rate limiting/capping | Sub-millisecond reads within 2-second SLA |
| ADR-05 | JWT with refresh rotation | Stateless validation + theft detection |
| ADR-06 | Append-only consent records | GDPR Art. 7 audit trail requirement |
| ADR-07 | Pub/Sub for async events | Decoupled services, guaranteed delivery |
| ADR-08 | OTP stored as SHA-256 hash | Security — codes unusable if DB compromised |

---

## BL11: Known Limitations (Post Sprint 1)

| LIM ID | Description | Impact | Planned Resolution |
|---|---|---|---|
| LIM-01 | Offers tab is placeholder (no content) | Users see empty state after registration | Sprint 2 EP-03 populates with geofenced offers |
| LIM-02 | History tab is placeholder (no content) | Users see empty state | Sprint 2 EP-04 adds redemption history |
| LIM-03 | No geofencing active | Location consent collected but not used yet | Sprint 2 EP-03 activates geofence monitoring |
| LIM-04 | No transaction data integration | Transaction consent collected but not used | Sprint 3 EP-05 integrates Open Banking |
| LIM-05 | No merchant-facing features | Offers must be created via direct DB/API | Sprint 3 EP-05 adds merchant dashboard |
| LIM-06 | No offer redemption flow | Cannot redeem offers | Sprint 2 EP-04 adds QR redemption |
| LIM-07 | Notification service has no offer triggers | Push infrastructure ready but no geofence events to trigger it | Sprint 2 EP-03 connects geofence events to notification pipeline |
| LIM-08 | No data deletion capability | GDPR deletion right not yet implemented | Sprint 3 EP-06 adds account deletion |

---

## BL12: Sprint 1 Delivery Metrics

| Metric | Target | Notes |
|---|---|---|
| Stories | 23 | EP-00 (5) + EP-01 (8) + EP-02 (10) |
| Story Points | 71 | Design system (16) + Auth (24) + Notifications (31) |
| APIs | 17 | 5 auth + 2 user + 4 consent/profile + 3 notification + 1 device + 2 health |
| Tables | 9 | Core data model for users, sessions, consent, notifications |
| Services | 4 | auth, notification, user, api-gateway |
| Integrations | 6 | APNs, FCM, SMS, Email, Redis, Cloud KMS |
| Pub/Sub Topics | 6 | User lifecycle + notification events |

---

## BL14: UX Wireframes & User Flows

**Artifact:** `wireframes/sprint1-wireframes.html`
**Design System:** Lloyds Banking Group Official Design System (lloyds-design-system.md)
**Screens:** 13 | **Flows:** 3

### Screen Inventory (Wireframed)

| Screen | Route | Flow | Stories | Status |
|---|---|---|---|---|
| Registration | /onboarding/register | Onboarding | US-006, US-013 | ✅ Wireframed |
| Registration (Error) | /onboarding/register | Onboarding | US-013 | ✅ Wireframed |
| OTP Verification | /onboarding/verify | Onboarding | US-007 | ✅ Wireframed |
| Biometric Enrollment | /onboarding/biometric | Onboarding | US-008 | ✅ Wireframed |
| Location Consent | /onboarding/consent-location | Onboarding | US-009 | ✅ Wireframed |
| Transaction Consent | /onboarding/consent-transaction | Onboarding | US-010 | ✅ Wireframed |
| Offers Tab (empty) | /offers | Main App | US-003 | ✅ Wireframed |
| History Tab (empty) | /history | Main App | US-003 | ✅ Wireframed |
| Profile Tab | /profile | Main App | US-019, US-020 | ✅ Wireframed |
| Settings Tab | /settings | Main App | US-017, US-018 | ✅ Wireframed |
| Notification Preferences | /settings/notifications | Main App | US-017, US-018 | ✅ Wireframed |
| Push Pre-Permission | /onboarding/push-permission | Push Permission | US-023 | ✅ Wireframed |
| Push Denied Fallback | (in-app banner) | Push Permission | US-023 | ✅ Wireframed |

### User Flows Documented

| Flow | Screens | Decision Points |
|---|---|---|
| Onboarding Journey | Register → OTP → Biometric → Location Consent → Transaction Consent → Push → Main App | Duplicate account, wrong OTP, skip biometric, deny location, deny transaction |
| Settings & Preferences | Settings → Notification Prefs / Location / Transaction / Export / Delete | Mode change, quiet hours, category toggles, OS permission dialog |
| Session Lifecycle | App Launch → Token check → Silent refresh or Biometric → Main App | Token valid, refresh valid, expired (re-auth) |

### Design System Tokens Applied

| Token | Value | Usage in Wireframes |
|---|---|---|
| --lloyds-green | #006A4D | Primary CTAs, active nav, focus rings, brand elements |
| --bg-app | #F5F5F5 | Screen backgrounds |
| --bg-surface | #FFFFFF | Cards, inputs, nav bars |
| --text-primary | #1A1A1A | Headings, body text |
| --text-error | #C0392B | Validation errors, destructive actions |
| --radius-md | 8px | Buttons, inputs |
| --radius-lg | 12px | Cards |
| Touch targets | 44×44px min | All interactive elements |

---

## BL15: Service Implementation

**Location:** `services/` · **Stack:** Java 21, Spring Boot 3.3, Hexagonal Architecture, CQRS  
**Total:** 96 files, 58 source, 13 tests, 6 migrations across 6 services

| Service | Source | Tests | Key Handlers |
|---|---|---|---|
| auth-service | 10 | 3 | RegisterUser, VerifyOtp, Login, RefreshToken, Logout, RecordConsent |
| user-service | 8 | 2 | UpdateProfile, GetProfile, UpdateNotificationPrefs, DeleteAccount, Export |
| notification-service | 9 | 2 | RegisterDevice, SendNotification, CheckFrequencyCap |
| offer-service | 10 | 2 | GetNearbyOffers, RedeemOffer, ConfirmRedemption, RateOffer, ReportOffer |
| geofence-service | 9 | 2 | CreateZone, ProcessGeofenceEvent, GetNearbyZones, GetCities |
| merchant-service | 12 | 2 | RegisterMerchant, CreateCampaign, PublishCampaign, GetAnalytics |

---

## BL16: UI Implementation

**Location:** `ui/`  
**Consumer App:** `ui/consumer-app/` · React 18, TypeScript 5, Vite 5, Tailwind CSS, TanStack Query, Zustand  
**Merchant Dashboard:** `ui/merchant-dashboard/` · React 18, TypeScript 5 (scaffold ready)

### Consumer App Structure
```
ui/consumer-app/src/
├── components/atoms/     Button, Input (Lloyds design system)
├── pages/onboarding/     RegisterPage, VerifyOtpPage, ConsentPage
├── pages/offers/         OffersPage (feed with empty/error/loading states)
├── stores/               authStore (Zustand + persist)
├── services/             api.ts (Axios + JWT interceptor + refresh rotation)
├── test/                 Button.test, Input.test, authStore.test
└── App.tsx               Router with AuthGuard + AppShell + bottom nav
```

### Key Patterns
- **Design tokens** via Tailwind config (Lloyds green, typography scale, spacing)
- **Auth flow**: Register → OTP → Biometric → Consent → Push → Main app
- **Token refresh**: Axios interceptor with silent refresh + rotation
- **Server state**: TanStack Query with 30s stale time
- **Forms**: React Hook Form + Zod validation
- **Accessibility**: aria-labels, role="alert", aria-invalid, focus management

---

## BL13: Sprint 2 Preview (What's Next)

Sprint 2 will add:
- **EP-03**: Geofence zone management, device-side monitoring, real-time offer matching pipeline, in-app offer feed, city boundary enforcement
- **EP-04**: QR code redemption, duplicate prevention, offer history, expiry automation, merchant confirmation

New tables expected: `geofence_zones`, `offers`, `campaigns`, `redemptions`, `offer_user_assignments`
New integrations: PostGIS spatial queries, Cloud Scheduler (expiry jobs)
New services: `geofence-service`, `offer-service`, `redemption-service`

This baseline will be updated at Sprint 2 completion with the full geofencing and redemption capabilities.
