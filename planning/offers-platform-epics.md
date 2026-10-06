# Smart Location-Based Offers Platform — Epics & Features

> Generated from `offers-platform-epics.json`. Content is reproduced from the source; headings and layout are added for readability.

## Overview

| Metric | Value |
|---|---|
| Total epics | 9 |
| Total features | 41 |
| Delivery sprints | 4 |
| Uncovered requirements | None |

### Delivery summary

The Smart Location-Based Offers Platform is delivered across 4 sprints with incremental production releases. Sprint 1 (EP-01 + EP-02) establishes the foundation: user authentication, consent management, push notification infrastructure, and user preferences — deployable as a standalone app with account management. Sprint 2 (EP-03 + EP-04) delivers the core MVP: geofencing engine, real-time offer delivery, QR-based redemption, and offer history — the platform is fully functional for end users with manually-created offers. Sprint 3 (EP-05 + EP-06) adds intelligence and self-service: transaction-based personalization, merchant self-service platform for campaign creation, analytics dashboard, and full GDPR/CCPA data rights — the platform can scale without manual offer management. Sprint 4 (EP-07 + EP-08) hardens for scale and adds engagement features: social sharing, wearables, review-based recommendations, auto-scaling to 1M users, monitoring, failover, and abuse prevention — production-ready for public launch at scale.

### Sprint roadmap

| Sprint | Epic | Title | Features | Requirements covered |
|---|---|---|---|---|
| 1 | [EP-00](#ep-00-design-system--app-shell) | Design System & App Shell | 2 | `NFR-08` |
| 1 | [EP-01](#ep-01-user-authentication--registration) | User Authentication & Registration | 5 | `FR-16`, `FR-20`, `NFR-03`, `NFR-04`, `NFR-05` |
| 1 | [EP-02](#ep-02-notification-infrastructure--user-preferences) | Notification Infrastructure & User Preferences | 5 | `FR-05`, `FR-09`, `FR-15`, `FR-24` |
| 2 | [EP-03](#ep-03-geofencing--real-time-offer-delivery) | Geofencing & Real-Time Offer Delivery | 5 | `FR-01`, `FR-14`, `FR-21`, `FR-22`, `FR-26` |
| 2 | [EP-04](#ep-04-offer-redemption--history) | Offer Redemption & History | 5 | `FR-04`, `FR-06`, `FR-23`, `FR-27` |
| 3 | [EP-05](#ep-05-personalization-engine--merchant-platform) | Personalization Engine & Merchant Platform | 5 | `FR-03`, `FR-10`, `FR-17`, `FR-18`, `FR-19`, `FR-25` |
| 3 | [EP-06](#ep-06-privacy-controls--data-rights) | Privacy Controls & Data Rights | 5 | `FR-02`, `FR-07`, `FR-08` |
| 4 | [EP-07](#ep-07-social-features--extended-channels) | Social Features & Extended Channels | 3 | `FR-11`, `FR-12`, `FR-13` |
| 4 | [EP-08](#ep-08-platform-resilience--operational-excellence) | Platform Resilience & Operational Excellence | 6 | `NFR-06`, `NFR-09`, `NFR-11` |

### Feature profile

| Attribute | Breakdown |
|---|---|
| Data sensitivity | Internal: 21, Confidential: 9, Restricted: 6, Public: 5 |
| User-facing | Yes: 25, No: 16 |
| Change type | new: 41 |
| Backward compatible | Yes: 41, No: 0 |

> **Note:** All features are greenfield. None list existing screens, APIs or tables affected, regression risks, an existing feature reference or a change description, so the per-feature sections leave those fields out.

> **About acceptance criteria:** The source JSON has no separate acceptance-criteria field. Each feature's `description` holds its testable behaviour (limits, timings, error handling), so that text is listed below as numbered **Specification & acceptance points**, verbatim.

## Table of contents

- [EP-00 — Design System & App Shell](#ep-00-design-system--app-shell) (Sprint 1)
  - F-00.1 — Design Token System & Component Library
  - F-00.2 — App Shell & Tab Navigation
- [EP-01 — User Authentication & Registration](#ep-01-user-authentication--registration) (Sprint 1)
  - F-01.1 — Phone/Email Registration
  - F-01.2 — OTP Verification
  - F-01.3 — Biometric Authentication Enrollment
  - F-01.4 — Consent Collection Flow
  - F-01.5 — Session Management & Token Refresh
- [EP-02 — Notification Infrastructure & User Preferences](#ep-02-notification-infrastructure--user-preferences) (Sprint 1)
  - F-02.1 — Push Notification Service Integration
  - F-02.2 — Notification Preference Controls
  - F-02.3 — Notification Frequency Capping
  - F-02.4 — User Profile & Spending Preferences
  - F-02.5 — Push Permission Request & Fallback
- [EP-03 — Geofencing & Real-Time Offer Delivery](#ep-03-geofencing--real-time-offer-delivery) (Sprint 2)
  - F-03.1 — Geofence Zone Management Service
  - F-03.2 — Device-Side Geofence Monitoring
  - F-03.3 — Real-Time Offer Matching Pipeline
  - F-03.4 — In-App Offer Feed
  - F-03.5 — Supported City Boundary Enforcement
- [EP-04 — Offer Redemption & History](#ep-04-offer-redemption--history) (Sprint 2)
  - F-04.1 — QR Code Offer Redemption
  - F-04.2 — Duplicate Redemption Prevention
  - F-04.3 — Offer History & Redemption Status
  - F-04.4 — Offer Expiry Automation
  - F-04.5 — Merchant Redemption Confirmation
- [EP-05 — Personalization Engine & Merchant Platform](#ep-05-personalization-engine--merchant-platform) (Sprint 3)
  - F-05.1 — Payment Provider Integration
  - F-05.2 — Transaction Analysis & Spending Patterns
  - F-05.3 — Merchant Registration & Onboarding
  - F-05.4 — Campaign & Offer Creation
  - F-05.5 — Merchant Analytics Dashboard
- [EP-06 — Privacy Controls & Data Rights](#ep-06-privacy-controls--data-rights) (Sprint 3)
  - F-06.1 — Location Tracking Settings
  - F-06.2 — Data Sharing Opt-Out
  - F-06.3 — Account & Data Deletion
  - F-06.4 — Offer Reporting
  - F-06.5 — Data Export (Portability)
- [EP-07 — Social Features & Extended Channels](#ep-07-social-features--extended-channels) (Sprint 4)
  - F-07.1 — Social Offer Sharing
  - F-07.2 — Review-Based Recommendations
  - F-07.3 — Wearable Device Notifications
- [EP-08 — Platform Resilience & Operational Excellence](#ep-08-platform-resilience--operational-excellence) (Sprint 4)
  - F-08.1 — Auto-Scaling & Load Management
  - F-08.2 — Monitoring, Alerting & Observability
  - F-08.3 — Automated Failover & Recovery
  - F-08.4 — Rate Limiting & Abuse Prevention
  - F-08.5 — Performance Optimization & Caching
  - F-08.6 — DR Testing & Chaos Engineering
- [Non-functional requirement mapping](#non-functional-requirement-mapping)
- [Requirements traceability matrix](#requirements-traceability-matrix)

---

## Epics

### EP-00 — Design System & App Shell

**Sprint:** 1 · **Features:** 2 · **Requirements covered:** `NFR-08`

#### Purpose

Establish the mobile app's visual foundation, navigation structure, and reusable component library. Unblocks all subsequent UI work by providing consistent patterns for screens, buttons, typography, and navigation.

#### In scope

- Design token system (colors, typography, spacing, elevation)
- Reusable UI component library (buttons, inputs, cards, modals, toasts)
- Tab bar navigation structure
- App shell with header, content area, and bottom navigation
- Loading states, empty states, and error state patterns
- Accessibility foundations (screen reader labels, touch targets, contrast)

#### Out of scope

- Screen-specific layouts — built within each epic
- Merchant dashboard design system — separate web component library
- Animation library — added incrementally per feature

#### Rationale

Rule 4 mandates a Design System epic in Sprint 1 for products with UI. Mobile apps need consistent navigation, component patterns, and accessibility foundations before feature screens are built. Without this, each epic would independently solve navigation and styling, creating inconsistency.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-00.1 | Design Token System & Component Library | Yes | Public | `NFR-08` |
| F-00.2 | App Shell & Tab Navigation | Yes | Public | `NFR-08` |

#### F-00.1 — Design Token System & Component Library

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-08` |
| Applicable NFRs | `NFR-02`, `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Public |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Define and implement design tokens: (1) Colors — primary (brand blue), secondary (teal for offers), success (green), error (red), neutral greys.
2. (2) Typography — heading (24/20/16pt bold), body (14pt regular), caption (12pt).
3. (3) Spacing — 4pt grid system (4, 8, 12, 16, 24, 32, 48).
4. (4) Elevation — 3 levels (flat, raised, floating).
5. Reusable components: Button (primary/secondary/ghost/danger, with loading state), TextInput (with label, error, helper text), Card (offer card, info card), Badge (status badges), Toast (success/error/info, auto-dismiss 3s).
6. All components support dark mode.
7. All touch targets minimum 44x44pt.
8. On validation failure: show inline error below input in red with icon.
9. On network error: show toast with retry action.

**Rationale**

Lloyds architecture mandates a shared design system. 44pt touch targets and contrast ratios from WCAG 2.1 AA. 4pt grid is the mobile standard (iOS/Material Design). Components defined here are reused across all subsequent epics, ensuring consistency.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-08: WCAG 2.1 AA Compliance — screen readers, high-contrast modes, adjustable font sizes

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Shared design system component library across brands)
- kb-L0-lloyds-enterprise-architecture §9 (Use shared design system, WCAG 2.1 AA)

</details>

#### F-00.2 — App Shell & Tab Navigation

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-08` |
| Applicable NFRs | `NFR-02`, `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Public |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. App shell structure: (1) Status bar (system).
2. (2) Header — screen title, optional back button, optional action button.
3. (3) Content area — scrollable, pull-to-refresh capable.
4. (4) Bottom tab bar — 4 tabs: Offers (feed icon), History (clock icon), Profile (person icon), Settings (gear icon).
5. Tab bar behavior: highlight active tab, badge on Offers tab showing unread count, hide on full-screen flows (QR display, onboarding).
6. Deep link handling: app links / universal links route to correct tab and screen.
7. Navigation stack: each tab maintains independent navigation history.
8. On app launch: open last active tab (persisted).
9. On notification tap: navigate to relevant offer in Offers tab.
10. Error state: if app fails to load, show retry screen with 'Try again' button and support contact link.

**Rationale**

4-tab structure covers the core user journeys: discover offers, track history, manage profile, control settings. Independent navigation stacks per tab is the native iOS/Android pattern. Deep linking required for notification tap → offer detail flow. Badge count drives engagement by showing unread offers.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-08: WCAG 2.1 AA — all interactive elements accessible and labeled

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

---

### EP-01 — User Authentication & Registration

**Sprint:** 1 · **Features:** 5 · **Requirements covered:** `FR-16`, `FR-20`, `NFR-03`, `NFR-04`, `NFR-05`

#### Purpose

Enable consumers to create accounts, verify identity, and securely authenticate. Foundation for all personalized features — every subsequent epic requires an authenticated user context.

#### In scope

- User registration (email/phone)
- OTP verification
- Biometric enrollment (Face ID/Touch ID)
- JWT session management with refresh tokens
- Consent collection during registration
- Account lockout after failed attempts

#### Out of scope

- Social login (Google, Apple) — future phase
- Merchant authentication — covered in EP-05
- Password reset via customer support — future phase

#### Rationale

FR-16 (auth) and FR-20 (consent) are tightly coupled — consent must be collected during registration. Sprint 1 because every other epic depends on authenticated users. NFR-03 (encryption), NFR-04 (GDPR), NFR-05 (CCPA) apply because auth handles credentials and PII.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-01.1 | Phone/Email Registration | Yes | Confidential | `FR-16` |
| F-01.2 | OTP Verification | Yes | Confidential | `FR-16` |
| F-01.3 | Biometric Authentication Enrollment | Yes | Restricted | `FR-16` |
| F-01.4 | Consent Collection Flow | Yes | Confidential | `FR-20` |
| F-01.5 | Session Management & Token Refresh | No | Restricted | `FR-16` |

#### F-01.1 — Phone/Email Registration

| Attribute | Value |
|---|---|
| Requirements covered | `FR-16` |
| Applicable NFRs | `NFR-03`, `NFR-04` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. User provides email or phone number to create account.
2. Validate format (E.164 for phone, RFC 5322 for email).
3. Create account in 'pending_verification' state.
4. Prevent duplicate registration by checking existing accounts.
5. Rate limit: 5 registration attempts per device per hour.
6. On success: trigger OTP verification flow.
7. On failure: show inline validation errors with specific guidance.

**Rationale**

Scoped to registration only (not verification) as these are separate user actions. Data sensitivity Confidential because phone/email are PII. Rate limiting per device prevents abuse while allowing legitimate retries.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-16: User Registration and Authentication — Users must be able to create an account and authenticate

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.1 (Security Layers — Cloud IAM, OAuth 2.0)
- kb-L0-epics-best-practices §2 (Compliance Checkpoints — PII handling)

</details>

#### F-01.2 — OTP Verification

| Attribute | Value |
|---|---|
| Requirements covered | `FR-16` |
| Applicable NFRs | `NFR-03`, `NFR-09` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Generate 6-digit numeric OTP with 90-second expiry.
2. Deliver via SMS (phone) or email.
3. Single-use — consumed on successful verification.
4. Rate limit: max 3 OTP requests per 10 minutes per phone/email.
5. Auto-focus on OTP input field.
6. Resend button with countdown timer.
7. On success: mark account as verified, proceed to consent collection.
8. On failure: show 'Invalid code' with remaining attempts (max 5).
9. After 5 failed attempts: block for 30 minutes.

**Rationale**

OTP parameters (6-digit, 90s, 3/10min rate limit) follow industry standards for mobile consumer apps. Separated from F-01.1 because verification is a distinct screen and user action.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-16: User Registration and Authentication

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.1 (Security — encryption in transit TLS 1.3)

</details>

#### F-01.3 — Biometric Authentication Enrollment

| Attribute | Value |
|---|---|
| Requirements covered | `FR-16` |
| Applicable NFRs | `NFR-03` |
| User-facing | Yes |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. After OTP verification, prompt user to enable Face ID (iOS) or fingerprint (Android) for subsequent logins.
2. Store biometric preference flag (not biometric data itself — uses OS keychain).
3. Fallback to PIN/password if biometric fails 3 times.
4. On success: store JWT refresh token in secure enclave.
5. On decline: proceed with email/password login for future sessions.

**Rationale**

Native biometric APIs (LocalAuthentication/BiometricPrompt) are the standard for consumer mobile apps. Data sensitivity Restricted because biometric preference + secure enclave token access is highest sensitivity. Biometric data never leaves device.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-16: User Registration and Authentication

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

#### F-01.4 — Consent Collection Flow

| Attribute | Value |
|---|---|
| Requirements covered | `FR-20` |
| Applicable NFRs | `NFR-04`, `NFR-05` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. During registration, present consent screens for: (1) Location data access — explain purpose, show 3 options (always/app-open/off), (2) Transaction data access — explain personalization benefit, binary opt-in/out.
2. Each consent recorded with timestamp, version, and granular purpose.
3. User can proceed with partial consent (location only, or transaction only) but personalization features degrade gracefully.
4. Consent records stored immutably for audit.
5. GDPR Article 7 compliant — consent must be freely given, specific, informed, unambiguous.

**Rationale**

Consent is collected during registration (not after) because GDPR requires consent before data processing begins. Granular consent (location separate from transaction) follows GDPR specificity requirement. Immutable storage for audit trail per GDPR Art. 7(1).

<details><summary>Sources cited</summary>

*Requirements used*

- FR-20: User Consent Management — transparent consent flows, explicit consent before accessing personal data

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.2 (Regulatory Compliance — UK GDPR/DPA 2018)
- kb-L0-epics-best-practices §3 (Regulatory Acceptance Criteria — GDPR Art. 6)

</details>

#### F-01.5 — Session Management & Token Refresh

| Attribute | Value |
|---|---|
| Requirements covered | `FR-16` |
| Applicable NFRs | `NFR-03`, `NFR-09` |
| User-facing | No |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Issue JWT access token (15-min expiry) and refresh token (30-day expiry) on successful authentication.
2. Refresh token rotation — each use issues new refresh token and invalidates old.
3. Store refresh token in device secure storage (Keychain/Keystore).
4. On token expiry: silent refresh if valid refresh token exists.
5. On refresh failure: redirect to biometric/login screen.
6. Support concurrent sessions across devices with per-device token tracking.
7. Logout invalidates all tokens for that device.

**Rationale**

JWT with refresh rotation is the standard pattern for mobile apps per Lloyds enterprise architecture (OAuth 2.0). 15-min access token balances security with UX. Restricted sensitivity because tokens grant system access.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-16: User Registration and Authentication

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Backend — OAuth 2.0/OpenID Connect)
- kb-L0-lloyds-enterprise-architecture §5.1 (Security — Cloud IAM)

</details>

---

### EP-02 — Notification Infrastructure & User Preferences

**Sprint:** 1 · **Features:** 5 · **Requirements covered:** `FR-05`, `FR-09`, `FR-15`, `FR-24`

#### Purpose

Build the push notification delivery pipeline (APNs/FCM), user preference management, and notification frequency controls. This is the primary channel for offer delivery and must be production-ready before geofencing goes live.

#### In scope

- Push notification service integration (APNs + FCM)
- Device token registration and management
- Notification preference settings (always-on vs app-open only)
- Frequency capping engine (1 per offer per day)
- User profile and spending category preferences
- Notification permission request flow with fallback

#### Out of scope

- Offer matching logic — covered in EP-03
- In-app notification center — covered in EP-04
- Wearable device notifications — covered in EP-07

#### Rationale

FR-05 (push), FR-09 (frequency limiting), FR-15 (preferences) form the notification delivery pipeline. Placed in Sprint 1 because Sprint 2's geofencing epic depends on a working notification channel. FR-24 (user profile) included here as preferences are managed in the same settings area.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-02.1 | Push Notification Service Integration | No | Internal | `FR-05` |
| F-02.2 | Notification Preference Controls | Yes | Internal | `FR-15` |
| F-02.3 | Notification Frequency Capping | No | Internal | `FR-09` |
| F-02.4 | User Profile & Spending Preferences | Yes | Confidential | `FR-24` |
| F-02.5 | Push Permission Request & Fallback | Yes | Internal | `FR-05`, `FR-15` |

#### F-02.1 — Push Notification Service Integration

| Attribute | Value |
|---|---|
| Requirements covered | `FR-05` |
| Applicable NFRs | `NFR-01`, `NFR-09` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Integrate with APNs (iOS) and FCM (Android) for push delivery.
2. Register device tokens on app launch and refresh on token rotation.
3. Support notification payloads: title (max 50 chars), body (max 150 chars), deep link URL, image URL (optional), action buttons (max 2).
4. Delivery confirmation tracking via delivery receipts.
5. Retry logic: 3 attempts with exponential backoff (1s, 5s, 30s).
6. Handle token invalidation (uninstall detection).
7. Batch sending support for campaign-wide notifications.

**Rationale**

APNs/FCM are the only viable push services (CON-05). Retry with exponential backoff follows distributed systems best practice. Batch support needed for NFR-07 (10K campaigns). Internal sensitivity as device tokens are system identifiers, not PII.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-05: Push Notification Delivery — support push notifications for relevant offers delivered within 2 seconds

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §7.1 (Pub/Sub events for async communication)
- kb-L0-lloyds-enterprise-architecture §3.2 (Microservices on GKE)

</details>

#### F-02.2 — Notification Preference Controls

| Attribute | Value |
|---|---|
| Requirements covered | `FR-15` |
| Applicable NFRs | `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Settings screen where users configure: (1) Notification mode — 'Always' (background push), 'App open only' (in-app only), 'Off' (no notifications).
2. (2) Quiet hours — start/end time when no notifications are sent.
3. (3) Category preferences — toggle notifications per offer category (dining, retail, entertainment, services).
4. Changes take effect immediately.
5. Default: 'Always' with no quiet hours, all categories enabled.
6. Persist preferences server-side for cross-device consistency.

**Rationale**

Three-mode control (always/app-open/off) directly from source. Quiet hours added as standard UX for notification-heavy apps. Category toggles enable granular control without full opt-out. Server-side persistence per Lloyds architecture (Cloud SQL for transactional data).

<details><summary>Sources cited</summary>

*Requirements used*

- FR-15: User Notification Preferences — control notification preferences including always-on push vs in-app only

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §9 (Key Architectural Decisions — consent management as first-class concern)

</details>

#### F-02.3 — Notification Frequency Capping

| Attribute | Value |
|---|---|
| Requirements covered | `FR-09` |
| Applicable NFRs | `NFR-01`, `NFR-06` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Engine that enforces: (1) Max 1 notification per offer per user per day — tracked via offer_id + user_id + date composite key.
2. (2) Max 5 total notifications per user per day (configurable).
3. (3) Minimum 30-minute gap between consecutive notifications to same user.
4. On cap reached: queue notification for next eligible window.
5. Frequency state stored in Redis (Memorystore) for sub-millisecond lookup.
6. Reset daily at midnight user's local timezone.

**Rationale**

Redis chosen for frequency state because sub-millisecond reads are needed within the 2-second delivery SLA (NFR-01). Daily cap of 5 and 30-min gap are UX best practices to prevent notification fatigue beyond the per-offer limit in FR-09.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-09: Notification Frequency Limiting — limit offer notifications to one per offer per day per user

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Memorystore/Redis for caching)

</details>

#### F-02.4 — User Profile & Spending Preferences

| Attribute | Value |
|---|---|
| Requirements covered | `FR-24` |
| Applicable NFRs | `NFR-08`, `NFR-04` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Profile screen with: (1) Display name (optional, max 50 chars).
2. (2) Preferred spending categories — multi-select from: Dining, Retail Fashion, Grocery, Entertainment, Health & Beauty, Travel, Services.
3. (3) Budget sensitivity — Low/Medium/High (affects offer value threshold shown).
4. (4) Preferred radius — how far user is willing to travel for offers (500m, 1km, 2km, 5km).
5. Profile completion is optional — system works with transaction data alone if profile is empty.
6. Changes saved on field blur (auto-save).

**Rationale**

Spending categories derived from source personas (dining, retail, entertainment). Preferred radius enables geofence relevance filtering. Confidential sensitivity because spending preferences reveal financial behavior patterns. Auto-save for mobile UX best practice.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-24: User Profile and Preference Management — view and update profile, set spending category preferences

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §9 (consent management as first-class concern)

</details>

#### F-02.5 — Push Permission Request & Fallback

| Attribute | Value |
|---|---|
| Requirements covered | `FR-05`, `FR-15` |
| Applicable NFRs | `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. On first app launch post-registration, present a pre-permission screen explaining the value of push notifications ('Get notified about deals near you').
2. If user grants OS permission: register device token, enable push delivery.
3. If user denies: show in-app banner explaining they can enable later in settings, default to in-app-only mode.
4. Track permission state.
5. If permission revoked later (detected on app foreground): update delivery mode to in-app-only automatically.
6. Never re-prompt OS permission dialog (iOS limitation).

**Rationale**

Pre-permission screen is critical for opt-in rates (industry best practice: 40-60% improvement). iOS only allows one OS permission prompt — must maximize first-ask success. Fallback to in-app mode ensures platform value even without push permission.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-05: Push Notification Delivery
- FR-15: User Notification Preferences

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

---

### EP-03 — Geofencing & Real-Time Offer Delivery

**Sprint:** 2 · **Features:** 5 · **Requirements covered:** `FR-01`, `FR-14`, `FR-21`, `FR-22`, `FR-26`

#### Purpose

Build the core geofencing engine that detects user proximity to merchant locations and triggers real-time offer delivery. This is the platform's primary differentiator — location-aware, context-sensitive offer matching and delivery within 2 seconds.

#### In scope

- Geofence zone creation and management
- Device-side geofence monitoring (entry/exit detection)
- Server-side offer matching pipeline
- Real-time offer delivery within 2-second SLA
- In-app offer feed (browsable offers)
- Geofence boundary enforcement (supported cities only)
- Time-of-day offer filtering

#### Out of scope

- Transaction-based personalization — covered in EP-05
- Offer redemption flow — covered in EP-04
- Merchant campaign creation — covered in EP-05
- Wearable proximity detection — covered in EP-07

#### Rationale

FR-01 (real-time delivery), FR-21 (zone management), FR-26 (event detection), FR-14 (boundary enforcement), FR-22 (in-app feed) form the complete geofencing delivery pipeline. Sprint 2 because it depends on EP-01 (auth) and EP-02 (notifications) being complete. This is the MVP's core value proposition.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-03.1 | Geofence Zone Management Service | No | Internal | `FR-21` |
| F-03.2 | Device-Side Geofence Monitoring | No | Restricted | `FR-26`, `FR-02` |
| F-03.3 | Real-Time Offer Matching Pipeline | No | Internal | `FR-01` |
| F-03.4 | In-App Offer Feed | Yes | Public | `FR-22` |
| F-03.5 | Supported City Boundary Enforcement | Yes | Public | `FR-14` |

#### F-03.1 — Geofence Zone Management Service

| Attribute | Value |
|---|---|
| Requirements covered | `FR-21` |
| Applicable NFRs | `NFR-07`, `NFR-11` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Backend service for CRUD operations on geofence zones.
2. Each zone has: zone_id, merchant_id, center_lat/lng, radius_meters (min 50m, max 5km), polygon_points (optional for non-circular zones), active_hours (start/end per day-of-week), status (active/paused/expired).
3. API endpoints: POST /zones, GET /zones/{id}, PUT /zones/{id}, DELETE /zones/{id}, GET /zones?lat={}&lng={}&radius={}.
4. Spatial indexing (PostGIS or equivalent) for efficient proximity queries.
5. Validate: no overlapping zones for same merchant, radius within bounds, valid coordinates.

**Rationale**

PostGIS spatial indexing enables efficient 'find zones near point' queries needed for real-time matching. Radius bounds (50m-5km) prevent abuse (too small = never triggers, too large = irrelevant). Internal sensitivity as zone data is merchant operational data, not PII.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-21: Geofence Zone Management — creation, modification, deletion of geofence zones with radius/polygon boundaries and active hours

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Cloud SQL PostgreSQL for transactional data)
- kb-L0-lloyds-enterprise-architecture §4.1 (Pub/Sub for async messaging)

</details>

#### F-03.2 — Device-Side Geofence Monitoring

| Attribute | Value |
|---|---|
| Requirements covered | `FR-26`, `FR-02` |
| Applicable NFRs | `NFR-01`, `NFR-10` |
| User-facing | No |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Native mobile module that registers active geofences with OS location services. iOS: CLLocationManager with CLCircularRegion (max 20 monitored regions — use region rotation for more).
2. Android: GeofencingClient with GeofencingRequest.
3. On geofence entry/exit: fire event to server with user_id, zone_id, event_type (enter/exit), timestamp, accuracy_meters.
4. Respect user's location tracking preference (always/app-open/off).
5. Battery optimization: use significant location changes for coarse tracking, precise geofencing only for registered zones.
6. Handle OS-level location permission changes gracefully.

**Rationale**

iOS 20-region limit is a hard platform constraint requiring rotation strategy. Restricted sensitivity because real-time location data is highly sensitive PII. Battery optimization critical per NFR-10 — significant location changes use less power than continuous GPS.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-26: Geofence Entry/Exit Event Detection — detect when user enters/exits geofenced area
- FR-02: User Location Tracking Controls

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

#### F-03.3 — Real-Time Offer Matching Pipeline

| Attribute | Value |
|---|---|
| Requirements covered | `FR-01` |
| Applicable NFRs | `NFR-01`, `NFR-06`, `NFR-11` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Event-driven pipeline triggered by geofence entry events.
2. Steps: (1) Receive entry event via Pub/Sub.
3. (2) Query active offers for zone_id where current time is within active_hours.
4. (3) Filter by user preferences (categories, notification mode).
5. (4) Apply frequency cap check (F-02.3).
6. (5) Rank remaining offers by relevance score (initially: recency + category match).
7. (6) Select top offer (or top 3 for in-app).
8. (7) Dispatch to notification service (F-02.1).
9. End-to-end latency budget: 2000ms total — event receipt 200ms, query 300ms, filter 200ms, rank 300ms, dispatch 500ms, delivery 500ms.
10. On pipeline failure: log error, do not notify user (silent failure — no degraded experience).

**Rationale**

Pub/Sub chosen as event bus per Lloyds architecture. Latency budget breakdown ensures each component has a clear SLA contributing to the 2-second total (NFR-01). Silent failure on pipeline error prevents confusing UX — user simply doesn't get a notification rather than seeing an error.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-01: Real-Time Location-Based Offer Delivery — deliver offers in real time based on location and time of day within 2 seconds

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §4.1 (Pub/Sub for event bus, Dataflow for stream processing)
- kb-L0-lloyds-enterprise-architecture §7.1 (Pub/Sub events for async communication)

</details>

#### F-03.4 — In-App Offer Feed

| Attribute | Value |
|---|---|
| Requirements covered | `FR-22` |
| Applicable NFRs | `NFR-02`, `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Public |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Scrollable feed screen showing available offers near the user's current location.
2. Each offer card shows: merchant name, offer title, discount value, distance from user, expiry countdown, category icon.
3. Sort options: nearest first (default), highest value, expiring soon.
4. Filter by category (matches user preference categories).
5. Pull-to-refresh updates location and re-queries.
6. Empty state: 'No offers nearby — try expanding your radius in settings'.
7. Pagination: 20 offers per page, infinite scroll.
8. Offers update when user moves significantly (>500m).

**Rationale**

Feed is the in-app delivery channel (source: 'push notifications or in-app'). 20-item pagination balances load time (NFR-02: 1s) with content density. 500m movement threshold prevents excessive API calls while keeping content fresh. Public sensitivity as offer content is merchant-published marketing material.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-22: In-App Offer Feed — browse available offers filtered by proximity, category, and relevance

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Frontend — shared design system, WCAG 2.1 AA)

</details>

#### F-03.5 — Supported City Boundary Enforcement

| Attribute | Value |
|---|---|
| Requirements covered | `FR-14` |
| Applicable NFRs | `NFR-06` |
| User-facing | Yes |
| Data sensitivity | Public |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Service that maintains a list of supported city boundaries (polygon coordinates).
2. On geofence event: check if user location falls within any supported city boundary.
3. If outside: suppress offer delivery, do not trigger matching pipeline.
4. Admin API to add/remove/modify supported cities.
5. Initial launch: configurable list of urban markets.
6. User-facing: if user opens app outside supported area, show 'Coming soon to your area' message on offer feed with option to be notified when their city is added.

**Rationale**

Boundary enforcement prevents irrelevant notifications (US6) and aligns with CON-03 (initial launch limited to select urban markets). 'Coming soon' UX retains users who travel outside supported areas rather than showing empty state.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-14: Geofence Boundary Enforcement — must not deliver offers outside supported cities

*Knowledge-base sections used*

- kb-L0-epics-best-practices §6 (Scoping — Geography dimension)

</details>

---

### EP-04 — Offer Redemption & History

**Sprint:** 2 · **Features:** 5 · **Requirements covered:** `FR-04`, `FR-06`, `FR-23`, `FR-27`

#### Purpose

Enable users to redeem offers at participating merchants and track their redemption history. Completes the core user journey: receive offer → redeem → view history. After this sprint, the MVP is functional end-to-end.

#### In scope

- QR code generation for offer redemption
- Merchant-side redemption confirmation
- Offer status lifecycle (active → redeemed → expired)
- Offer history screen with filters
- Duplicate redemption prevention
- Offer expiry automation

#### Out of scope

- Merchant POS integration — future phase (manual confirmation for MVP)
- Savings calculator/gamification — covered in EP-07
- Offer reporting (invalid/expired) — covered in EP-06

#### Rationale

FR-04 (redemption), FR-06 (history), FR-23 (expiry), FR-27 (duplicate prevention) form the complete redemption lifecycle. Sprint 2 alongside EP-03 because redemption is the monetization event — without it, geofencing delivers no business value. Together EP-03 + EP-04 deliver a complete MVP.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-04.1 | QR Code Offer Redemption | Yes | Internal | `FR-04` |
| F-04.2 | Duplicate Redemption Prevention | No | Internal | `FR-27` |
| F-04.3 | Offer History & Redemption Status | Yes | Confidential | `FR-06` |
| F-04.4 | Offer Expiry Automation | No | Internal | `FR-23` |
| F-04.5 | Merchant Redemption Confirmation | Yes | Internal | `FR-04` |

#### F-04.1 — QR Code Offer Redemption

| Attribute | Value |
|---|---|
| Requirements covered | `FR-04` |
| Applicable NFRs | `NFR-02`, `NFR-03` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. When user taps 'Redeem' on an offer: generate a unique, time-limited QR code containing: redemption_token (UUID), offer_id, user_id (hashed), expiry_timestamp (5 minutes from generation).
2. Display QR code full-screen with brightness auto-maximized.
3. Countdown timer showing time remaining.
4. Merchant scans QR via their app/device → validates token against server → marks as redeemed.
5. On successful scan: show 'Offer Redeemed!' confirmation with confetti animation, offer details, and savings amount.
6. On expiry: QR disappears, user can regenerate.
7. On already-redeemed: show 'This offer has already been used'.

**Rationale**

QR code is the assumed redemption mechanism (ASM-06). 5-minute expiry prevents screenshot sharing/abuse. Hashed user_id in QR prevents PII exposure if QR is photographed. Brightness maximization is standard for QR-based mobile payments.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-04: Instant Offer Redemption at Merchant — redeem instantly via mobile interface, offer marked as redeemed, merchant receives confirmation

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.1 (Security — encryption in transit)

</details>

#### F-04.2 — Duplicate Redemption Prevention

| Attribute | Value |
|---|---|
| Requirements covered | `FR-27` |
| Applicable NFRs | `NFR-09`, `NFR-06` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Server-side enforcement: each offer instance can only be redeemed once per user.
2. On redemption attempt: check redemption_log table for existing entry with (offer_id, user_id).
3. If exists: reject with 409 Conflict, return 'Already redeemed' message.
4. Use database-level unique constraint on (offer_id, user_id) as final safety net.
5. For multi-use offers (e.g., '10% off, use 3 times'): track redemption_count against max_redemptions.
6. Idempotency: if same redemption_token is submitted twice within 5 minutes, return success (not duplicate error) — handles network retries.

**Rationale**

Database-level constraint is the last line of defense against race conditions. Idempotency key (redemption_token) prevents false duplicate errors from network retries — critical for mobile apps with unreliable connectivity.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-27: Duplicate Redemption Prevention — prevent users from redeeming same offer more than once

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Cloud SQL PostgreSQL — unique constraints)

</details>

#### F-04.3 — Offer History & Redemption Status

| Attribute | Value |
|---|---|
| Requirements covered | `FR-06` |
| Applicable NFRs | `NFR-02`, `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. History screen showing all user's offers grouped by status: (1) Active — available for redemption, sorted by expiry (soonest first).
2. (2) Redeemed — with date, merchant, savings amount.
3. (3) Expired — greyed out, with 'Expired on [date]'.
4. Each entry shows: offer title, merchant name, discount value, status badge, date.
5. Filter by: status (all/active/redeemed/expired), date range, category.
6. Total savings counter at top ('You've saved £X.XX').
7. Pull-to-refresh.
8. Empty state per tab: 'No [active/redeemed/expired] offers yet'.
9. Pagination: 20 items, infinite scroll.

**Rationale**

Three-tab grouping (active/redeemed/expired) directly from AC4 which mentions 'all redeemed and active offers with status and date'. Savings counter adds engagement value. Confidential sensitivity because redemption history reveals spending patterns.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-06: Offer History and Redemption Status — view all redeemed and active offers with status and date

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (WCAG 2.1 AA accessibility)

</details>

#### F-04.4 — Offer Expiry Automation

| Attribute | Value |
|---|---|
| Requirements covered | `FR-23` |
| Applicable NFRs | `NFR-09`, `NFR-11` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Scheduled job (Cloud Scheduler → Cloud Function) runs every 15 minutes: (1) Query offers where expiry_timestamp < now() AND status = 'active'.
2. (2) Batch update status to 'expired'.
3. (3) Remove from user's active offer list.
4. (4) Update offer feed cache.
5. For offers expiring within 1 hour: send 'Expiring soon' notification (if user has notifications enabled and hasn't been notified for this offer today).
6. Merchant-side: expired offers automatically stop triggering geofence matches.
7. Audit log: record all status transitions with timestamp.

**Rationale**

15-minute batch interval balances timeliness with system load. 'Expiring soon' notification drives urgency and redemption rates (key KPI). Cloud Scheduler + Cloud Function is the GCP-native pattern for scheduled jobs per Lloyds architecture.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-23: Offer Expiry and Lifecycle Management — automatically expire offers, remove from user views, prevent redemption of expired offers

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §4.1 (Cloud Composer/Airflow for workflow orchestration)

</details>

#### F-04.5 — Merchant Redemption Confirmation

| Attribute | Value |
|---|---|
| Requirements covered | `FR-04` |
| Applicable NFRs | `NFR-02`, `NFR-09` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Merchant-facing flow: (1) Merchant opens their app/dashboard.
2. (2) Scans customer's QR code using device camera.
3. (3) System validates: token not expired, not already redeemed, offer is active, merchant_id matches.
4. (4) On valid: show offer details + 'Confirm Redemption' button.
5. (5) On confirm: mark redeemed, notify customer device in real-time (WebSocket/push).
6. (6) On invalid: show specific error ('Expired', 'Already used', 'Wrong merchant').
7. Merchant sees daily redemption count on their home screen.
8. Offline fallback: merchant can enter redemption code manually (8-char alphanumeric displayed below QR on customer device).

**Rationale**

Merchant confirmation is explicitly required by AC3 ('merchant receives confirmation'). Manual code fallback handles scenarios where merchant device camera fails or connectivity is poor. Real-time customer notification creates a satisfying 'ding' moment confirming the redemption.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-04: Instant Offer Redemption — merchant receiving confirmation of redemption

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §7.1 (REST APIs for synchronous calls)

</details>

---

### EP-05 — Personalization Engine & Merchant Platform

**Sprint:** 3 · **Features:** 5 · **Requirements covered:** `FR-03`, `FR-10`, `FR-17`, `FR-18`, `FR-19`, `FR-25`

#### Purpose

Integrate transaction data analysis for personalized offer recommendations and build the merchant-facing platform for campaign creation and analytics. Transforms the MVP from generic location-based delivery to intelligent, personalized engagement.

#### In scope

- Payment provider integration for transaction data
- Transaction analysis and spending pattern extraction
- Personalized offer ranking based on spending history
- Merchant registration and onboarding workflow
- Merchant campaign creation and management
- Merchant analytics dashboard
- Offer content authoring tools

#### Out of scope

- ML-based recommendation models — future phase (rules-based for MVP)
- Merchant billing/payments — future phase
- Merchant API for programmatic campaign management — future phase

#### Rationale

FR-03 (transaction analysis), FR-19 (payment integration) enable personalization. FR-10 (analytics), FR-17 (onboarding), FR-18 (campaign management), FR-25 (offer creation) form the merchant platform. Sprint 3 because: (1) MVP works without personalization (Sprint 2 uses category matching), (2) merchant self-service reduces operational burden for scaling beyond initial partners.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-05.1 | Payment Provider Integration | No | Restricted | `FR-19` |
| F-05.2 | Transaction Analysis & Spending Patterns | No | Confidential | `FR-03` |
| F-05.3 | Merchant Registration & Onboarding | Yes | Confidential | `FR-17` |
| F-05.4 | Campaign & Offer Creation | Yes | Internal | `FR-18`, `FR-25` |
| F-05.5 | Merchant Analytics Dashboard | Yes | Internal | `FR-10` |

#### F-05.1 — Payment Provider Integration

| Attribute | Value |
|---|---|
| Requirements covered | `FR-19` |
| Applicable NFRs | `NFR-03`, `NFR-04`, `NFR-12` |
| User-facing | No |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Integrate with Open Banking APIs to securely access user transaction history (with consent).
2. Support: account information API (read-only transaction list).
3. Data retrieved: merchant_name, amount, currency, date, category_code (MCC).
4. Sync frequency: daily batch + on-demand refresh.
5. Store normalized transactions in analytics database (BigQuery).
6. Handle: provider downtime (circuit breaker, cached data), consent revocation (stop sync, retain historical), multiple accounts (user links 1+ bank accounts).
7. PSD2/Open Banking compliant: registered as AISP (Account Information Service Provider).

**Rationale**

Open Banking AISP is the regulated mechanism for transaction data access (ASM-02). Restricted sensitivity because transaction data reveals complete financial behavior. Circuit breaker pattern per Lloyds architecture for external service resilience. BigQuery for analytics storage per Lloyds data platform.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-19: Payment Provider Integration — integrate with payment providers to securely access and analyze user transaction history

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §7.2 (Open Banking PSD2 — REST APIs via developer portal)
- kb-L0-lloyds-enterprise-architecture §3.2 (Circuit breakers for external calls)

</details>

#### F-05.2 — Transaction Analysis & Spending Patterns

| Attribute | Value |
|---|---|
| Requirements covered | `FR-03` |
| Applicable NFRs | `NFR-11`, `NFR-12`, `NFR-04` |
| User-facing | No |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Batch processing pipeline (Dataflow) that analyzes user transactions to extract: (1) Top spending categories (MCC code grouping).
2. (2) Average transaction value per category.
3. (3) Spending frequency patterns (daily/weekly/monthly).
4. (4) Preferred merchants (by visit frequency).
5. (5) Time-of-day spending patterns.
6. Output: user_spending_profile stored in BigQuery, refreshed daily.
7. Profile feeds into offer matching pipeline (F-03.3) as additional ranking signal.
8. Privacy: only aggregated patterns stored, not raw transactions (data minimization per NFR-12).
9. Users can view their spending profile summary in-app.

**Rationale**

Dataflow (Apache Beam) is Lloyds' standard for batch/stream processing. Storing aggregated patterns rather than raw transactions follows GDPR data minimization (NFR-12). MCC codes are the standard merchant category classification used by all payment networks.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-03: Transaction History Analysis for Personalization — analyze transaction history to personalize offer recommendations based on spending patterns

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §4.1 (Dataflow for stream processing, BigQuery for analytics)
- kb-L0-lloyds-enterprise-architecture §4.2 (ML Pipeline — data sources to BigQuery)

</details>

#### F-05.3 — Merchant Registration & Onboarding

| Attribute | Value |
|---|---|
| Requirements covered | `FR-17` |
| Applicable NFRs | `NFR-03`, `NFR-04` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Web-based merchant registration flow: (1) Business details: company name, registration number, business type, address.
2. (2) Contact: primary contact name, email, phone.
3. (3) Verification: business document upload (certificate of incorporation, proof of address).
4. (4) Quality review: admin reviews submission within 48 hours.
5. (5) Approval: merchant receives credentials, can create campaigns.
6. Status tracking: submitted → under_review → approved/rejected.
7. Rejection includes reason and option to resubmit.
8. Rate limit: 3 submissions per business registration number.
9. Admin dashboard for review queue with approve/reject/request-more-info actions.

**Rationale**

Quality review is an explicit constraint (CON-04). 48-hour SLA balances merchant experience with review thoroughness. Web-based per ASM-01 (merchant dashboard is desktop). Confidential sensitivity because business registration details are commercially sensitive.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-17: Merchant Registration and Onboarding — register and undergo quality review before campaigns go live

*Knowledge-base sections used*

- kb-L0-epics-best-practices §2 (Compliance Checkpoints — onboarding review)

</details>

#### F-05.4 — Campaign & Offer Creation

| Attribute | Value |
|---|---|
| Requirements covered | `FR-18`, `FR-25` |
| Applicable NFRs | `NFR-07` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Merchant dashboard for creating offer campaigns: (1) Offer content: title (max 60 chars), description (max 200 chars), terms (max 500 chars), hero image (upload, 16:9, max 2MB), discount type (percentage/fixed/BOGOF), discount value.
2. (2) Targeting: geofence zones (select from merchant's registered locations), active hours per day, target categories (user spending categories to match against).
3. (3) Limits: max redemptions total, max per user, validity period (start/end dates).
4. (4) Budget: daily notification cap, total campaign budget (notifications × cost).
5. Preview: show how offer appears on user's device.
6. Save as draft, schedule, or publish immediately.
7. Edit live campaigns (changes take effect within 5 minutes).

**Rationale**

Combined FR-18 and FR-25 as they represent a single merchant workflow (create campaign = create offer + set targeting). React web app per Lloyds architecture for internal tools. 5-minute propagation delay allows for cache invalidation across the geofencing pipeline.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-18: Merchant Campaign Management — create, configure, manage campaigns with geofence areas, offer hours, target audience
- FR-25: Offer Content Creation — title, description, terms, images, discount value, validity period, redemption limits

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (React SPA for internal/colleague-facing apps)

</details>

#### F-05.5 — Merchant Analytics Dashboard

| Attribute | Value |
|---|---|
| Requirements covered | `FR-10` |
| Applicable NFRs | `NFR-02`, `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Dashboard showing campaign performance metrics: (1) Overview: total impressions, total redemptions, redemption rate, active campaigns count.
2. (2) Per-campaign: impressions over time (line chart), redemptions over time, unique users reached, peak hours heatmap, geographic distribution of redemptions.
3. (3) User engagement: average time from notification to redemption, repeat visitor rate, category breakdown of redeeming users.
4. (4) Export: CSV download of raw data, PDF report generation.
5. Date range selector (last 7d, 30d, 90d, custom).
6. Real-time updates for today's data (polling every 60s).
7. Comparison mode: compare two campaigns side-by-side.

**Rationale**

Metrics directly from AC9 (impressions, redemptions, user engagement). BigQuery backend for analytics per Lloyds data platform. Internal sensitivity because campaign performance data is merchant business intelligence but not PII. 60s polling for today's data balances freshness with server load.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-10: Merchant Analytics Dashboard — campaign analytics including impressions, redemptions, and user engagement metrics

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §4.1 (BigQuery for analytics)
- kb-L0-lloyds-enterprise-architecture §1.1 (Visualisation — Power BI, Tableau, Looker)

</details>

---

### EP-06 — Privacy Controls & Data Rights

**Sprint:** 3 · **Features:** 5 · **Requirements covered:** `FR-02`, `FR-07`, `FR-08`

#### Purpose

Implement full GDPR/CCPA data rights: opt-out, data deletion, offer reporting, and consent modification. Ensures regulatory compliance and builds user trust through transparent data controls.

#### In scope

- Location tracking settings modification
- Data sharing opt-out with graceful degradation
- Account and data deletion (30-day window)
- Offer reporting (expired/invalid)
- Consent modification and re-consent flows
- Data export (GDPR portability)

#### Out of scope

- Initial consent collection — covered in EP-01 (F-01.4)
- Regulatory audit reporting — future phase
- Automated compliance monitoring — future phase

#### Rationale

FR-02 (location controls), FR-07 (opt-out/deletion), FR-08 (reporting) are privacy and compliance features. Sprint 3 because: (1) Core functionality must exist first (Sprints 1-2), (2) GDPR rights must be available before public launch, (3) Pairs with EP-05 which introduces transaction data processing — users need controls over that data.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-06.1 | Location Tracking Settings | Yes | Confidential | `FR-02` |
| F-06.2 | Data Sharing Opt-Out | Yes | Restricted | `FR-07` |
| F-06.3 | Account & Data Deletion | Yes | Restricted | `FR-07` |
| F-06.4 | Offer Reporting | Yes | Internal | `FR-08` |
| F-06.5 | Data Export (Portability) | Yes | Confidential | `FR-07` |

#### F-06.1 — Location Tracking Settings

| Attribute | Value |
|---|---|
| Requirements covered | `FR-02` |
| Applicable NFRs | `NFR-04`, `NFR-10` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Privacy settings screen with location control: (1) Always — app monitors geofences in background (requires OS 'Always' permission).
2. (2) App open only — geofencing active only when app is in foreground.
3. (3) Off — no location data collected, offer feed shows all offers in supported city (no proximity sorting).
4. Changing from 'Always' to 'Off': immediately stop background monitoring, clear registered geofences from OS, confirm to user.
5. Changing to 'Always': trigger OS permission dialog if not already granted.
6. Show current OS permission state and link to system settings if mismatched.
7. Persist choice server-side.

**Rationale**

Three options directly from FR-02. OS permission state sync is critical because user can change permissions in system settings outside the app. Immediate effect on 'Off' selection per GDPR — cannot continue processing after withdrawal. Confidential because location preference reveals privacy sensitivity.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-02: User Location Tracking Controls — control settings with options: always-on, app-open only, or off

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.2 (UK GDPR/DPA 2018)
- kb-L0-epics-best-practices §3 (GDPR Art. 6 — consent before optional processing)

</details>

#### F-06.2 — Data Sharing Opt-Out

| Attribute | Value |
|---|---|
| Requirements covered | `FR-07` |
| Applicable NFRs | `NFR-04`, `NFR-05`, `NFR-12` |
| User-facing | Yes |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Privacy settings toggle: 'Share transaction data for personalized offers'.
2. On opt-out: (1) Stop syncing transaction data from payment providers.
3. (2) Delete stored spending profile (user_spending_profile in BigQuery).
4. (3) Revert offer matching to category-preference-only mode (no transaction-based ranking).
5. (4) Show confirmation: 'Personalized offers disabled.
6. You'll still receive offers based on your location and category preferences.' On opt-back-in: re-trigger consent flow (F-01.4 transaction consent), restart sync.
7. Opt-out takes effect within 1 hour (batch deletion of profile data).
8. Audit log: record opt-out timestamp and scope.

**Rationale**

Graceful degradation (category-only matching) ensures app remains useful after opt-out — prevents user churn. 1-hour deletion window for batch processing efficiency while meeting 'without undue delay' GDPR requirement. Restricted sensitivity because opt-out action itself reveals privacy concerns.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-07: Data Sharing Opt-Out — opt out of data sharing, stopping collection of transaction and location data, disabling personalized offers

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §9 (consent management as first-class concern)
- kb-L0-epics-best-practices §3 (GDPR Art. 17 — erasure within 30 days)

</details>

#### F-06.3 — Account & Data Deletion

| Attribute | Value |
|---|---|
| Requirements covered | `FR-07` |
| Applicable NFRs | `NFR-04`, `NFR-05` |
| User-facing | Yes |
| Data sensitivity | Restricted |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Settings option: 'Delete my account and data'.
2. Flow: (1) Confirmation screen explaining what will be deleted and 7-day cooling-off period.
3. (2) Enter password/biometric to confirm identity.
4. (3) Account enters 'pending_deletion' state — user can cancel within 7 days by logging back in.
5. (4) After 7 days: automated deletion job removes all user data across all services within 30 days total (per AC10).
6. Deletion scope: profile, preferences, consent records, transaction data, offer history, redemption records, device tokens, notification history.
7. Retained (anonymized): aggregated analytics contributions.
8. Notify user via email when deletion is complete.
9. Downstream service notification via Pub/Sub event 'user.deleted'.

**Rationale**

30-day window from AC10. 7-day cooling-off prevents accidental deletion (industry best practice). Pub/Sub event for cross-service deletion per Lloyds event-driven architecture. Anonymized analytics retained per GDPR allowance for statistical purposes. Restricted sensitivity as deletion is an irreversible action on all user data.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-07: Account Deletion — request account/data deletion with completion within 30 days

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §7.1 (Pub/Sub events for async communication)
- kb-L0-epics-best-practices §3 (GDPR Art. 17 — erasure within 30 days, downstream processors notified)

</details>

#### F-06.4 — Offer Reporting

| Attribute | Value |
|---|---|
| Requirements covered | `FR-08` |
| Applicable NFRs | `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. On any offer card (feed or history): 'Report issue' action.
2. Report types: (1) Expired — offer no longer valid at merchant.
3. (2) Invalid — terms don't match what merchant offers.
4. (3) Inappropriate — offensive or misleading content.
5. (4) Other — free text (max 500 chars).
6. On submit: (1) Offer flagged in admin queue.
7. (2) User sees confirmation: 'Thanks for reporting.
8. We'll review within 48 hours.' (3) If 3+ reports on same offer: auto-pause offer pending review.
9. (4) Admin reviews: dismiss report, warn merchant, or deactivate offer.
10. (5) Reporter notified of outcome.
11. Rate limit: max 10 reports per user per day (prevent abuse).

**Rationale**

Report types cover all scenarios from AC6 plus 'inappropriate' for content moderation. Auto-pause at 3 reports balances merchant protection with quality control. 48-hour review SLA matches merchant onboarding review timeline. Rate limiting prevents weaponized reporting against competitors.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-08: Report Expired or Invalid Offers — report, receive confirmation, offer flagged for review

*Knowledge-base sections used*

- kb-L0-epics-best-practices §4 (Risk — Reputational risk from inappropriate content)

</details>

#### F-06.5 — Data Export (Portability)

| Attribute | Value |
|---|---|
| Requirements covered | `FR-07` |
| Applicable NFRs | `NFR-04` |
| User-facing | Yes |
| Data sensitivity | Confidential |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Settings option: 'Download my data'.
2. Generates a machine-readable export (JSON) containing: profile information, consent records with timestamps, offer history, redemption history, spending preferences, notification preferences.
3. Excludes: raw transaction data (belongs to payment provider), system-generated IDs, internal analytics.
4. Generation is async — user receives push notification when ready (typically < 5 minutes).
5. Download link valid for 24 hours.
6. Max 1 export request per 7 days.
7. Format compliant with GDPR Article 20 (structured, commonly used, machine-readable).

**Rationale**

GDPR Article 20 requires data portability in machine-readable format. JSON chosen as commonly used and machine-readable. 7-day rate limit prevents abuse of export generation (resource-intensive). 24-hour link expiry limits exposure window for sensitive data download.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-07: Data Sharing Opt-Out and Account Deletion — GDPR data rights

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.2 (UK GDPR/DPA 2018)
- kb-L0-epics-best-practices §3 (GDPR data portability)

</details>

---

### EP-07 — Social Features & Extended Channels

**Sprint:** 4 · **Features:** 3 · **Requirements covered:** `FR-11`, `FR-12`, `FR-13`

#### Purpose

Add social sharing, wearable device support, and review-based recommendations. These are value-add features that increase engagement and viral growth but are not required for core platform operation.

#### In scope

- Social sharing of offers (native share sheet)
- Personalized recommendations from reviews/ratings
- Wearable device integration (Apple Watch, Wear OS)
- Referral tracking from shared offers

#### Out of scope

- Full social network features (friends list, messaging)
- User-generated content moderation at scale
- Third-party wearable SDKs beyond Apple/Google

#### Rationale

FR-11 (social sharing), FR-12 (review-based recommendations), FR-13 (wearables) are all Nice-to-Have priority. Sprint 4 because they add engagement value on top of a fully functional platform. No other features depend on these.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-07.1 | Social Offer Sharing | Yes | Public | `FR-11` |
| F-07.2 | Review-Based Recommendations | Yes | Internal | `FR-12` |
| F-07.3 | Wearable Device Notifications | Yes | Internal | `FR-13` |

#### F-07.1 — Social Offer Sharing

| Attribute | Value |
|---|---|
| Requirements covered | `FR-11` |
| Applicable NFRs | `NFR-04` |
| User-facing | Yes |
| Data sensitivity | Public |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Share button on each offer card.
2. Triggers native share sheet (iOS UIActivityViewController / Android Intent.ACTION_SEND).
3. Share content: offer title, merchant name, discount value, deep link to offer in app.
4. Deep link handling: if recipient has app → open offer detail.
5. If not → app store page with offer_id preserved for post-install attribution.
6. Track: shares per offer, installs from shares, redemptions from shared offers.
7. Shared offers do not bypass frequency caps for the recipient.
8. Privacy: sharer's identity not revealed to recipient (anonymous sharing).

**Rationale**

Native share sheet is the standard mobile pattern — no need to build custom sharing UI. Deep linking with deferred attribution enables viral growth measurement. Anonymous sharing protects privacy (GDPR — no PII shared without consent). Public sensitivity as offer content is already public marketing material.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-11: Social Sharing of Offers — share offers with friends via social channels

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

#### F-07.2 — Review-Based Recommendations

| Attribute | Value |
|---|---|
| Requirements covered | `FR-12` |
| Applicable NFRs | `NFR-08` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. After redemption (24 hours later): prompt user to rate the offer experience (1-5 stars) and optional text review (max 200 chars).
2. Ratings feed into offer ranking: (1) Offers with >4.0 average rating get ranking boost.
3. (2) Offers with <2.5 average rating get ranking penalty.
4. (3) Offers with <2.0 after 10+ ratings: flag for admin review.
5. Display: average rating and review count on offer cards.
6. User can view their past reviews in profile.
7. Merchant sees aggregate ratings in analytics dashboard.
8. Moderation: reviews with flagged keywords held for manual review before publishing.

**Rationale**

24-hour delay for review prompt gives user time to experience the offer. Rating thresholds (4.0 boost, 2.5 penalty, 2.0 flag) are standard marketplace quality signals. Keyword moderation prevents inappropriate content without blocking all reviews. Internal sensitivity as reviews are user-generated content visible to other users.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-12: Personalized Recommendations from Reviews — recommendations based on user reviews and ratings

*Knowledge-base sections used*

- kb-L0-epics-best-practices §4 (Reputational risk — content moderation)

</details>

#### F-07.3 — Wearable Device Notifications

| Attribute | Value |
|---|---|
| Requirements covered | `FR-13` |
| Applicable NFRs | `NFR-10` |
| User-facing | Yes |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Companion app for Apple Watch (watchOS) and Wear OS: (1) Receive offer notifications on wrist with: merchant name, discount value, distance.
2. (2) Quick actions: 'View in app' (opens phone app), 'Dismiss'.
3. (3) Complication showing nearest active offer count.
4. (4) No redemption on watch (QR too small) — 'Open on phone to redeem' action.
5. Notification delivery: leverages existing push infrastructure (APNs/FCM handle watch routing automatically for paired devices).
6. Settings: user can disable watch notifications independently of phone notifications.

**Rationale**

Watch notifications are automatically routed by APNs/FCM for paired devices — minimal backend changes needed. No QR on watch because screen size makes scanning impractical. Complication provides glanceable value. Independent notification toggle respects user preference granularity.

<details><summary>Sources cited</summary>

*Requirements used*

- FR-13: Wearable Device Integration — integrate with wearable devices for proximity-based notifications

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.1 (Native mobile — Swift iOS, Kotlin Android)

</details>

---

### EP-08 — Platform Resilience & Operational Excellence

**Sprint:** 4 · **Features:** 6 · **Requirements covered:** `NFR-06`, `NFR-09`, `NFR-11`

#### Purpose

Harden the platform for production scale: automated failover, monitoring, alerting, performance optimization, and abuse prevention. Ensures the platform meets its 99.9% uptime SLA and handles 1M concurrent users.

#### In scope

- Auto-scaling configuration for all services
- Health checks and automated failover
- Monitoring dashboards and alerting
- Performance optimization and caching
- Rate limiting and abuse prevention
- Chaos engineering and DR testing
- Location spoofing detection

#### Out of scope

- Multi-region deployment — future phase
- Custom SLA tiers per merchant — future phase
- AI-based anomaly detection — future phase

#### Rationale

NFR-06 (1M users), NFR-09 (99.9% uptime), NFR-11 (event throughput) are cross-cutting resilience concerns. Sprint 4 because: (1) Must have working system to optimize, (2) Production hardening is the final step before public launch, (3) Monitoring requires all services to be deployed to instrument.

#### Features at a glance

| ID | Feature | User-facing | Data sensitivity | Requirements |
|---|---|---|---|---|
| F-08.1 | Auto-Scaling & Load Management | No | Internal | `NFR-06` |
| F-08.2 | Monitoring, Alerting & Observability | No | Internal | `NFR-09` |
| F-08.3 | Automated Failover & Recovery | No | Internal | `NFR-09` |
| F-08.4 | Rate Limiting & Abuse Prevention | No | Internal | `NFR-06` |
| F-08.5 | Performance Optimization & Caching | No | Internal | `NFR-11` |
| F-08.6 | DR Testing & Chaos Engineering | No | Internal | `NFR-09` |

#### F-08.1 — Auto-Scaling & Load Management

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-06` |
| Applicable NFRs | `NFR-09`, `NFR-11` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. GKE Horizontal Pod Autoscaler (HPA) configuration for all services: (1) Geofence matching service: scale on CPU (target 60%) and custom metric (event queue depth).
2. Min 3 pods, max 50.
3. (2) Notification service: scale on queue depth.
4. Min 2 pods, max 30.
5. (3) API gateway: scale on request rate.
6. Min 3 pods, max 20.
7. (4) Offer feed service: scale on request latency p95.
8. Min 2 pods, max 15.
9. Vertical Pod Autoscaler (VPA) for memory optimization.
10. Pod Disruption Budgets: min 2 available for all critical services.
11. Pre-scaling rules for known peak times (lunch 12-2pm, evening 5-8pm).

**Rationale**

GKE HPA is the standard auto-scaling mechanism per Lloyds architecture. Custom metrics (queue depth, latency) provide more responsive scaling than CPU alone for event-driven workloads. Pre-scaling for known peaks prevents cold-start latency during rush hours.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-06: Concurrent User Capacity — support 1 million concurrent users without degradation

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §1.2 (GKE for container orchestration)
- kb-L0-lloyds-enterprise-architecture §3.3 (Cloud Monitoring, Prometheus)

</details>

#### F-08.2 — Monitoring, Alerting & Observability

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-09` |
| Applicable NFRs | `NFR-01`, `NFR-06` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Comprehensive observability stack: (1) Metrics: Cloud Monitoring dashboards for each service — request rate, error rate, latency (p50/p95/p99), queue depth, active connections.
2. (2) Logging: structured JSON logs to Cloud Logging with correlation IDs across services.
3. (3) Tracing: Cloud Trace for end-to-end request tracing (geofence event → notification delivery).
4. (4) Alerting: PagerDuty integration.
5. Critical alerts: error rate >1%, latency p95 >2s, service down.
6. Warning alerts: error rate >0.5%, latency p95 >1.5s, disk >80%.
7. (5) SLA dashboard: real-time uptime calculation, monthly SLA report generation.
8. (6) Business metrics: redemption rate, notification delivery rate, active users.

**Rationale**

Full observability stack directly from Lloyds enterprise architecture (Cloud Monitoring, Logging, Trace). Alert thresholds derived from NFR-01 (2s delivery SLA) — alert at 1.5s gives 500ms response window. Correlation IDs enable tracing a single geofence event through the entire pipeline.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-09: System Uptime — 99.9% uptime with automated failover and recovery

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.3 (Cloud Monitoring, Cloud Logging, Cloud Trace, Prometheus)
- kb-L0-lloyds-enterprise-architecture §1.1 (Observability stack)

</details>

#### F-08.3 — Automated Failover & Recovery

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-09` |
| Applicable NFRs | `NFR-06`, `NFR-11` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. High availability configuration: (1) Multi-zone GKE cluster (3 zones minimum).
2. (2) Cloud SQL HA: regional instance with automatic failover (RPO=0, RTO<60s).
3. (3) Redis (Memorystore): HA with automatic failover.
4. (4) Pub/Sub: inherently multi-zone (no configuration needed).
5. (5) Health check endpoints: /health (liveness), /ready (readiness) on all services.
6. (6) Circuit breakers: Resilience4j on all external calls (payment providers, APNs/FCM) — open after 5 failures in 30s, half-open after 60s.
7. (7) Graceful degradation: if payment provider down → skip personalization, use category matching only.
8. If notification service degraded → queue and retry.

**Rationale**

Multi-zone GKE is standard for 99.9% uptime. Circuit breaker pattern explicitly mentioned in Lloyds architecture for external calls. Graceful degradation ensures core functionality (location-based offers) works even when personalization services are down.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-09: System Uptime — automated failover and recovery for critical services

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Circuit breakers for external calls)
- kb-L0-lloyds-enterprise-architecture §1.2 (GKE)

</details>

#### F-08.4 — Rate Limiting & Abuse Prevention

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-06` |
| Applicable NFRs | `NFR-03`, `NFR-09` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Multi-layer rate limiting: (1) API gateway (Cloud Armor): 100 req/s per user, 1000 req/s per IP.
2. (2) Service-level: per-endpoint limits (e.g., redemption: 10/min per user).
3. (3) Location spoofing detection: flag events where user 'teleports' >50km in <5 minutes, or submits events from known VPN/proxy IPs, or has GPS accuracy >100m consistently.
4. On spoofing detection: suppress offer delivery, log for review, do not notify user (avoid revealing detection).
5. (4) Redemption fraud: flag users with >10 redemptions/day, or redemptions at merchants >20km from last known location.
6. (5) Bot detection: device fingerprinting, behavioral analysis (tap patterns, session duration).

**Rationale**

Cloud Armor is Lloyds' WAF/DDoS protection layer. Location spoofing is the primary abuse vector for geofence-based platforms (GAP-11). Silent suppression (no user notification) prevents attackers from learning detection thresholds. Multi-layer approach catches different attack types at appropriate levels.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-06: Concurrent User Capacity — 1M users without degradation (implies abuse prevention at scale)

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §5.1 (Cloud Armor WAF/DDoS)
- kb-L0-epics-best-practices §4 (Operational risk — fraud screening)

</details>

#### F-08.5 — Performance Optimization & Caching

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-11` |
| Applicable NFRs | `NFR-01`, `NFR-02` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Caching strategy: (1) Offer feed: Redis cache with 5-minute TTL, invalidated on new offer publish or expiry.
2. (2) Geofence zones: in-memory cache on matching service (refreshed every 60s via Pub/Sub subscription).
3. (3) User preferences: Redis cache with 1-hour TTL (invalidated on change).
4. (4) Merchant analytics: BigQuery materialized views refreshed hourly.
5. (5) CDN: offer images served via Cloud CDN with 24-hour cache.
6. Database optimization: read replicas for offer queries, connection pooling (HikariCP, max 20 per pod).
7. Query optimization: spatial indexes on geofence table, composite indexes on (user_id, offer_id, status) for history queries.
8. Target: all API responses <200ms at p95 (excluding notification delivery).

**Rationale**

Redis caching per Lloyds architecture (Memorystore). TTL values balance freshness with load reduction. In-memory geofence cache on matching service eliminates network hop in the critical 2-second path. Cloud CDN for static assets (images) is standard GCP pattern. 200ms API target leaves 1800ms budget for the async notification pipeline.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-11: Geofence Event Processing Throughput — process events for 1M users within 2-second SLA

*Knowledge-base sections used*

- kb-L0-lloyds-enterprise-architecture §3.2 (Memorystore Redis for caching)
- kb-L0-lloyds-enterprise-architecture §4.1 (BigQuery for analytics)

</details>

#### F-08.6 — DR Testing & Chaos Engineering

| Attribute | Value |
|---|---|
| Requirements covered | `NFR-09` |
| Applicable NFRs | `NFR-06` |
| User-facing | No |
| Data sensitivity | Internal |
| Change type | new |
| Backward compatible | Yes |

**Specification & acceptance points**

1. Quarterly DR test plan: (1) Simulate zone failure — verify traffic shifts to remaining zones within 60s.
2. (2) Simulate database failover — verify RTO <60s, zero data loss.
3. (3) Simulate notification service outage — verify offers queue and deliver on recovery.
4. (4) Simulate payment provider outage — verify graceful degradation to category-only matching.
5. Chaos engineering (monthly): inject latency (500ms) on random service, kill random pods, simulate network partition between services.
6. Success criteria: no user-visible errors during any test, all alerts fire correctly, recovery is automatic.
7. Document results and remediation actions.

**Rationale**

Quarterly DR testing per DORA Article 11 (applicable to financial services). Chaos engineering validates that failover mechanisms actually work under realistic conditions. Monthly cadence for chaos catches regressions from deployments. Documentation requirement for audit trail.

<details><summary>Sources cited</summary>

*Requirements used*

- NFR-09: System Uptime — 99.9% uptime with automated failover and recovery

*Knowledge-base sections used*

- kb-L0-epics-best-practices §3 (DORA Art. 11 — ICT business continuity plan tested quarterly, RTO/RPO met during DR test)

</details>

---

## Non-functional requirement mapping

| NFR | Title | Applies to epics |
|---|---|---|
| NFR-01 | Offer Notification Delivery Latency (2s) | `EP-02`, `EP-03`, `EP-08` |
| NFR-02 | App Screen Load Time (1s) | `EP-00`, `EP-03`, `EP-04`, `EP-05` |
| NFR-03 | Data Encryption in Transit and at Rest | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07`, `EP-08` |
| NFR-04 | GDPR Compliance | `EP-01`, `EP-02`, `EP-05`, `EP-06`, `EP-07` |
| NFR-05 | CCPA Compliance | `EP-01`, `EP-05`, `EP-06` |
| NFR-06 | Concurrent User Capacity (1M) | `EP-03`, `EP-08` |
| NFR-07 | Simultaneous Campaign Capacity (10K) | `EP-03`, `EP-05` |
| NFR-08 | WCAG 2.1 AA Compliance | `EP-00`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07` |
| NFR-09 | System Uptime (99.9%) | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-08` |
| NFR-10 | Battery Efficiency | `EP-03`, `EP-06` |
| NFR-11 | Geofence Event Processing Throughput | `EP-03`, `EP-08` |
| NFR-12 | Location Data Minimization | `EP-03`, `EP-05`, `EP-06` |

### NFR-01 — Offer Notification Delivery Latency (2s)

**Applies to:** `EP-02`, `EP-03`, `EP-08`

**Implementation notes:** EP-02: notification service must deliver within its 500ms budget. EP-03: matching pipeline must complete within 1500ms. EP-08: caching and optimization ensure latency under load.

**Rationale:** The 2-second SLA spans the entire pipeline from geofence event to notification delivery. EP-02 owns the delivery leg, EP-03 owns the matching leg, EP-08 ensures it holds at scale.

### NFR-02 — App Screen Load Time (1s)

**Applies to:** `EP-00`, `EP-03`, `EP-04`, `EP-05`

**Implementation notes:** EP-00 establishes performant component patterns (lazy loading, virtualized lists). All user-facing screens (offer feed, history, merchant dashboard) must render within 1 second. Requires efficient API responses, pagination, and client-side caching.

**Rationale:** EP-00 sets the performance baseline for UI components. Applies to epics with data-heavy screens. EP-01/EP-02 screens are simple forms that inherently load fast.

### NFR-03 — Data Encryption in Transit and at Rest

**Applies to:** `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07`, `EP-08`

**Implementation notes:** TLS 1.3 for all API communication. AES-256 for data at rest in Cloud SQL and BigQuery. Encryption keys managed via Cloud KMS. Applied universally across all services.

**Rationale:** Security NFR applies to all epics — every service handles data that must be encrypted. No epic is exempt from this requirement.

### NFR-04 — GDPR Compliance

**Applies to:** `EP-01`, `EP-02`, `EP-05`, `EP-06`, `EP-07`

**Implementation notes:** EP-01: consent collection, lawful basis. EP-02: preference data handling. EP-05: transaction data processing (legitimate interest or consent). EP-06: data rights (erasure, portability, opt-out). EP-07: social sharing privacy.

**Rationale:** GDPR applies wherever personal data is collected, processed, or shared. EP-03/EP-04 process data but under consent already collected in EP-01. EP-06 is the primary GDPR rights implementation.

### NFR-05 — CCPA Compliance

**Applies to:** `EP-01`, `EP-05`, `EP-06`

**Implementation notes:** EP-01: 'Do Not Sell' notice at registration. EP-05: transaction data qualifies as 'sale' under CCPA if shared with merchants. EP-06: right to delete, right to know.

**Rationale:** CCPA applies to California residents. Key touchpoints are data collection (EP-01), data sharing with third parties (EP-05 merchant analytics), and data rights (EP-06).

### NFR-06 — Concurrent User Capacity (1M)

**Applies to:** `EP-03`, `EP-08`

**Implementation notes:** EP-03: matching pipeline must handle event throughput from 1M users. EP-08: auto-scaling, load balancing, and caching to support concurrent connections.

**Rationale:** Scalability primarily affects the real-time event processing pipeline (EP-03) and requires infrastructure hardening (EP-08). Other epics benefit from EP-08's scaling but don't independently need to address 1M users.

### NFR-07 — Simultaneous Campaign Capacity (10K)

**Applies to:** `EP-03`, `EP-05`

**Implementation notes:** EP-03: geofence matching must efficiently query across 10K active campaigns. EP-05: campaign management must support 10K concurrent campaigns without UI degradation.

**Rationale:** 10K campaigns affects the matching query performance (EP-03 spatial queries) and the merchant platform's ability to manage campaigns at scale (EP-05).

### NFR-08 — WCAG 2.1 AA Compliance

**Applies to:** `EP-00`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07`

**Implementation notes:** EP-00 establishes accessibility foundations (touch targets, contrast, screen reader labels). All subsequent epics with user-facing screens inherit these patterns. Minimum touch targets 44x44pt. Color contrast ratio ≥4.5:1 for text.

**Rationale:** EP-00 provides the design system with accessibility baked in. All other epics with user-facing screens consume these accessible components. EP-01 has minimal UI (forms use EP-00 components). EP-08 has no user-facing components.

### NFR-09 — System Uptime (99.9%)

**Applies to:** `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-08`

**Implementation notes:** EP-08 is the primary implementation (failover, monitoring, DR). Other epics must implement health checks and graceful degradation. 99.9% = max 8.76 hours downtime/year.

**Rationale:** Uptime is a system-wide concern but primarily implemented in EP-08. Core path epics (EP-01 auth, EP-02 notifications, EP-03 geofencing, EP-04 redemption) must each be individually resilient.

### NFR-10 — Battery Efficiency

**Applies to:** `EP-03`, `EP-06`

**Implementation notes:** EP-03: optimize geofence monitoring (significant location changes, region rotation). EP-06: location tracking settings allow users to reduce battery impact by choosing 'app-open only'.

**Rationale:** Battery drain is caused by location monitoring (EP-03) and mitigated by user controls (EP-06). Other epics don't directly impact battery.

### NFR-11 — Geofence Event Processing Throughput

**Applies to:** `EP-03`, `EP-08`

**Implementation notes:** EP-03: pipeline architecture must support high throughput. EP-08: auto-scaling and caching ensure throughput under peak load.

**Rationale:** Throughput is an architectural concern (EP-03 pipeline design) and an operational concern (EP-08 scaling). Both must work together to meet the SLA.

### NFR-12 — Location Data Minimization

**Applies to:** `EP-03`, `EP-05`, `EP-06`

**Implementation notes:** EP-03: only store geofence events (not continuous location). EP-05: spending profiles use aggregates, not raw data. EP-06: deletion removes all location-derived data.

**Rationale:** Data minimization applies wherever personal data is processed. EP-03 handles location data, EP-05 handles transaction data, EP-06 implements the deletion that enforces minimization.

---

## Requirements traceability matrix

| Requirement | Title | Epic | Features |
|---|---|---|---|
| FR-01 | Real-Time Location-Based Offer Delivery | EP-03 | `F-03.3` |
| FR-02 | User Location Tracking Controls | EP-03 | `F-03.2`, `F-06.1` |
| FR-03 | Transaction History Analysis for Personalization | EP-05 | `F-05.2` |
| FR-04 | Instant Offer Redemption at Merchant | EP-04 | `F-04.1`, `F-04.5` |
| FR-05 | Push Notification Delivery | EP-02 | `F-02.1`, `F-02.5` |
| FR-06 | Offer History and Redemption Status | EP-04 | `F-04.3` |
| FR-07 | Data Sharing Opt-Out | EP-06 | `F-06.2`, `F-06.3`, `F-06.5` |
| FR-08 | Report Expired or Invalid Offers | EP-06 | `F-06.4` |
| FR-09 | Notification Frequency Limiting | EP-02 | `F-02.3` |
| FR-10 | Merchant Analytics Dashboard | EP-05 | `F-05.5` |
| FR-11 | Social Sharing of Offers | EP-07 | `F-07.1` |
| FR-12 | Personalized Recommendations from Reviews | EP-07 | `F-07.2` |
| FR-13 | Wearable Device Integration | EP-07 | `F-07.3` |
| FR-14 | Geofence Boundary Enforcement | EP-03 | `F-03.5` |
| FR-15 | User Notification Preferences | EP-02 | `F-02.2`, `F-02.5` |
| FR-16 | User Registration and Authentication | EP-01 | `F-01.1`, `F-01.2`, `F-01.3`, `F-01.5` |
| FR-17 | Merchant Registration and Onboarding | EP-05 | `F-05.3` |
| FR-18 | Merchant Campaign Management | EP-05 | `F-05.4` |
| FR-19 | Payment Provider Integration | EP-05 | `F-05.1` |
| FR-20 | User Consent Management | EP-01 | `F-01.4` |
| FR-21 | Geofence Zone Management | EP-03 | `F-03.1` |
| FR-22 | In-App Offer Feed | EP-03 | `F-03.4` |
| FR-23 | Offer Expiry and Lifecycle Management | EP-04 | `F-04.4` |
| FR-24 | User Profile and Preference Management | EP-02 | `F-02.4` |
| FR-25 | Offer Content Creation | EP-05 | `F-05.4` |
| FR-26 | Geofence Entry/Exit Event Detection | EP-03 | `F-03.2` |
| FR-27 | Duplicate Redemption Prevention | EP-04 | `F-04.2` |
