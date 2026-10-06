# Smart Location-Based Offers Platform for Personalized Customer Engagement — Requirements

> Generated from `offers-platform-requirements.json`. Content is reproduced from the source; headings and layout are added for readability. The **Delivered by** links come from the traceability matrix in `offers-platform-epics.json` (see [offers-platform-epics.md](offers-platform-epics.md)).

## Overview

A mobile app that integrates geofencing, behavioral analytics, and transaction data to deliver targeted, real-time offers to users based on their location, activity, and spending patterns, with seamless redemption at partner merchants.

| Category | Count |
|---|---|
| Functional requirements | 27 |
| Non-functional requirements | 12 |
| Constraints | 6 |
| Assumptions | 10 |
| Open gaps | 16 |

### Priority breakdown

| Priority | Functional | Non-functional |
|---|---|---|
| Must-Have | 19 | 11 |
| Should-Have | 5 | 1 |
| Nice-to-Have | 3 | 0 |

### Baseline summary

| Attribute | Value |
|---|---|
| Mode | greenfield |
| Baseline KB version | — |
| New requirements | 27 |
| Enhancement requirements | 0 |
| Existing requirements | 0 |
| Regression risks | None |

> **Note:** Every functional requirement is classified `new` and has no baseline reference or baseline impact, so the per-requirement sections leave those fields out.

> **About acceptance criteria:** The source JSON has no separate acceptance-criteria field. Each requirement's **Requirement** statement is its testable definition, and the **Source** quote shows the original brief text it came from. The feature-level detail is in the epics document.

> **Action needed:** 10 of 10 assumptions need confirmation, and 16 gaps need answers from stakeholders. See [Assumptions](#assumptions) and [Gaps & open questions](#gaps--open-questions).

## Table of contents

- [Functional requirements](#functional-requirements)
- [Non-functional requirements](#non-functional-requirements)
- [Constraints](#constraints)
- [Assumptions](#assumptions)
- [Gaps & open questions](#gaps--open-questions)

---

## Functional requirements

| ID | Title | Priority | User-facing | Delivered by |
|---|---|---|---|---|
| [FR-01](#fr-01-real-time-location-based-offer-delivery) | Real-Time Location-Based Offer Delivery | Must-Have | Yes | EP-03 |
| [FR-02](#fr-02-user-location-tracking-controls) | User Location Tracking Controls | Must-Have | Yes | EP-03 |
| [FR-03](#fr-03-transaction-history-analysis-for-personalization) | Transaction History Analysis for Personalization | Must-Have | No | EP-05 |
| [FR-04](#fr-04-instant-offer-redemption-at-merchant) | Instant Offer Redemption at Merchant | Must-Have | Yes | EP-04 |
| [FR-05](#fr-05-push-notification-delivery) | Push Notification Delivery | Must-Have | Yes | EP-02 |
| [FR-06](#fr-06-offer-history-and-redemption-status) | Offer History and Redemption Status | Must-Have | Yes | EP-04 |
| [FR-07](#fr-07-data-sharing-opt-out-and-account-deletion) | Data Sharing Opt-Out and Account Deletion | Must-Have | Yes | EP-06 |
| [FR-08](#fr-08-report-expired-or-invalid-offers) | Report Expired or Invalid Offers | Should-Have | Yes | EP-06 |
| [FR-09](#fr-09-notification-frequency-limiting) | Notification Frequency Limiting | Should-Have | No | EP-02 |
| [FR-10](#fr-10-merchant-analytics-dashboard) | Merchant Analytics Dashboard | Should-Have | Yes | EP-05 |
| [FR-11](#fr-11-social-sharing-of-offers) | Social Sharing of Offers | Nice-to-Have | Yes | EP-07 |
| [FR-12](#fr-12-personalized-recommendations-from-reviews) | Personalized Recommendations from Reviews | Nice-to-Have | Yes | EP-07 |
| [FR-13](#fr-13-wearable-device-integration) | Wearable Device Integration | Nice-to-Have | Yes | EP-07 |
| [FR-14](#fr-14-geofence-boundary-enforcement) | Geofence Boundary Enforcement | Should-Have | No | EP-03 |
| [FR-15](#fr-15-user-notification-preferences) | User Notification Preferences | Must-Have | Yes | EP-02 |
| [FR-16](#fr-16-user-registration-and-authentication) | User Registration and Authentication | Must-Have | Yes | EP-01 |
| [FR-17](#fr-17-merchant-registration-and-onboarding) | Merchant Registration and Onboarding | Must-Have | Yes | EP-05 |
| [FR-18](#fr-18-merchant-campaign-management) | Merchant Campaign Management | Must-Have | Yes | EP-05 |
| [FR-19](#fr-19-payment-provider-integration) | Payment Provider Integration | Must-Have | No | EP-05 |
| [FR-20](#fr-20-user-consent-management) | User Consent Management | Must-Have | Yes | EP-01 |
| [FR-21](#fr-21-geofence-zone-management) | Geofence Zone Management | Must-Have | No | EP-03 |
| [FR-22](#fr-22-in-app-offer-feed) | In-App Offer Feed | Must-Have | Yes | EP-03 |
| [FR-23](#fr-23-offer-expiry-and-lifecycle-management) | Offer Expiry and Lifecycle Management | Must-Have | No | EP-04 |
| [FR-24](#fr-24-user-profile-and-preference-management) | User Profile and Preference Management | Should-Have | Yes | EP-02 |
| [FR-25](#fr-25-offer-content-creation-by-merchants) | Offer Content Creation by Merchants | Must-Have | Yes | EP-05 |
| [FR-26](#fr-26-geofence-entryexit-event-detection) | Geofence Entry/Exit Event Detection | Must-Have | No | EP-03 |
| [FR-27](#fr-27-duplicate-redemption-prevention) | Duplicate Redemption Prevention | Must-Have | Yes | EP-04 |

### FR-01 Real-Time Location-Based Offer Delivery

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.3` |

**Requirement:** The system must deliver offers to users in real time based on their current location and time of day when they enter a geofenced area during merchant offer hours

**Source**

> “The app must deliver location-based offers to users in real time based on their current location and time of day”
>
> — *Functional Requirements - Must Have - FR1*

**Rationale:** Core capability of the platform. Priority Must-Have from explicit 'must' language. Combines FR1 and AC1 which describe the same capability.

### FR-02 User Location Tracking Controls

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.2`, `F-06.1` |

**Requirement:** The system must allow users to control location tracking settings with options: always-on, app-open only, or off

**Source**

> “The app must allow users to control location tracking settings (always-on, app-open only, or off)”
>
> — *Functional Requirements - Must Have - FR2*

**Rationale:** Privacy control capability. Must-Have from explicit 'must' language. Maps to AC2 and US2.

### FR-03 Transaction History Analysis for Personalization

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.2` |

**Requirement:** The system must analyze user transaction history to personalize offer recommendations based on spending patterns

**Source**

> “The app must analyze user transaction history to personalize offer recommendations”
>
> — *Functional Requirements - Must Have - FR3*

**Rationale:** Backend analytics capability that drives personalization. Must-Have from explicit 'must' language. Maps to US5.

### FR-04 Instant Offer Redemption at Merchant

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-04 (Offer Redemption & History) |
| Delivered by features | `F-04.1`, `F-04.5` |

**Requirement:** Users must be able to redeem offers instantly via a mobile interface at participating merchants, with the offer marked as redeemed and merchant receiving confirmation

**Source**

> “Users must be able to redeem offers instantly via a mobile interface at participating merchants”
>
> — *Functional Requirements - Must Have - FR4*

**Rationale:** Core redemption flow. Must-Have from explicit 'must' language. Maps to US3 and AC3.

### FR-05 Push Notification Delivery

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-02 (Notification Infrastructure & User Preferences) |
| Delivered by features | `F-02.1`, `F-02.5` |

**Requirement:** The system must support push notifications for relevant offers delivered within 2 seconds of a qualifying geofence event

**Source**

> “The app must support push notifications for relevant offers”
>
> — *Functional Requirements - Must Have - FR5*

**Rationale:** Primary delivery channel for offers. Must-Have from explicit 'must' language. 2-second target from NFR Performance section.

### FR-06 Offer History and Redemption Status

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-04 (Offer Redemption & History) |
| Delivered by features | `F-04.3` |

**Requirement:** Users must be able to view their offer history including all redeemed and active offers with status and date

**Source**

> “Users must be able to view their offer history and redemption status”
>
> — *Functional Requirements - Must Have - FR6*

**Rationale:** User transparency feature. Must-Have from explicit 'must' language. Maps to US4 and AC4.

### FR-07 Data Sharing Opt-Out and Account Deletion

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-06 (Privacy Controls & Data Rights) |
| Delivered by features | `F-06.2`, `F-06.3`, `F-06.5` |

**Requirement:** Users must be able to opt out of data sharing at any time (stopping collection of transaction and location data, disabling personalized offers) and request account/data deletion with completion within 30 days

**Source**

> “The app must allow users to opt out of data sharing and delete their account/data”
>
> — *Functional Requirements - Must Have - FR7*

**Rationale:** Privacy and compliance capability. Must-Have from explicit 'must' language. Maps to US10, AC5, and AC10. 30-day deletion window from AC10.

### FR-08 Report Expired or Invalid Offers

| Attribute | Value |
|---|---|
| Priority | Should-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-06 (Privacy Controls & Data Rights) |
| Delivered by features | `F-06.4` |

**Requirement:** Users should be able to report expired or invalid offers, receive confirmation of report submission, and have the offer flagged for review

**Source**

> “The app should allow users to report expired or invalid offers”
>
> — *Functional Requirements - Should Have - FR8*

**Rationale:** Quality control mechanism. Should-Have from explicit 'should' language. Maps to US8 and AC6.

### FR-09 Notification Frequency Limiting

| Attribute | Value |
|---|---|
| Priority | Should-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-02 (Notification Infrastructure & User Preferences) |
| Delivered by features | `F-02.3` |

**Requirement:** The system should limit offer notifications to one per offer per day per user, suppressing duplicate notifications when re-entering the same geofenced area

**Source**

> “The app should limit offer notifications to one per offer per day per user”
>
> — *Functional Requirements - Should Have - FR9*

**Rationale:** Anti-spam mechanism. Should-Have from explicit 'should' language. Maps to US9 and AC7.

### FR-10 Merchant Analytics Dashboard

| Attribute | Value |
|---|---|
| Priority | Should-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.5` |

**Requirement:** Merchants should have access to a dashboard displaying campaign analytics including impressions, redemptions, and user engagement metrics

**Source**

> “Merchants should have access to a dashboard with analytics on offer performance and user engagement”
>
> — *Functional Requirements - Should Have - FR10*

**Rationale:** Merchant-facing capability. Should-Have from explicit 'should' language. Maps to US7 and AC9.

### FR-11 Social Sharing of Offers

| Attribute | Value |
|---|---|
| Priority | Nice-to-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-07 (Social Features & Extended Channels) |
| Delivered by features | `F-07.1` |

**Requirement:** Users could share offers with friends via social channels

**Source**

> “The app could support social sharing of offers with friends”
>
> — *Functional Requirements - Nice to Have - FR11*

**Rationale:** Viral growth feature. Nice-to-Have from explicit 'could' language.

### FR-12 Personalized Recommendations from Reviews

| Attribute | Value |
|---|---|
| Priority | Nice-to-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-07 (Social Features & Extended Channels) |
| Delivered by features | `F-07.2` |

**Requirement:** The system could provide personalized recommendations based on user reviews and ratings

**Source**

> “The app could provide personalized recommendations based on user reviews and ratings”
>
> — *Functional Requirements - Nice to Have - FR12*

**Rationale:** Enhanced personalization. Nice-to-Have from explicit 'could' language.

### FR-13 Wearable Device Integration

| Attribute | Value |
|---|---|
| Priority | Nice-to-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-07 (Social Features & Extended Channels) |
| Delivered by features | `F-07.3` |

**Requirement:** The system could integrate with wearable devices for proximity-based notifications

**Source**

> “The app could integrate with wearable devices for proximity-based notifications”
>
> — *Functional Requirements - Nice to Have - FR13*

**Rationale:** Extended device support. Nice-to-Have from explicit 'could' language.

### FR-14 Geofence Boundary Enforcement

| Attribute | Value |
|---|---|
| Priority | Should-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.5` |

**Requirement:** The system must not deliver offers to users when they are outside supported cities or geofenced areas

**Source**

> “As a User, Not receive offers when outside supported cities, Prevents irrelevant notifications”
>
> — *User Stories - US6*

**Rationale:** Extracted from US6 as a distinct testable capability not covered by other FRs. Should-Have based on Medium priority in source.

### FR-15 User Notification Preferences

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-02 (Notification Infrastructure & User Preferences) |
| Delivered by features | `F-02.2`, `F-02.5` |

**Requirement:** Users must be able to control notification preferences including always-on push notifications versus in-app only delivery

**Source**

> “Allow users to control notification preferences (always-on vs. app-open only)”
>
> — *Proposed Solution - Key Decisions*

**Rationale:** Distinct from FR-02 (location tracking control). This is about notification delivery channel preference. Must-Have as it's listed as a key decision.

### FR-16 User Registration and Authentication

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-01 (User Authentication & Registration) |
| Delivered by features | `F-01.1`, `F-01.2`, `F-01.3`, `F-01.5` |

**Requirement:** Users must be able to create an account and authenticate to access personalized features, offer history, and privacy settings

**Source**

> “Given a user is logged in, When They navigate to the offer history screen”
>
> — *Acceptance Criteria - AC4*

**Rationale:** AC4 implies authentication exists ('logged in'). Multiple features depend on user identity. Must-Have as it's a prerequisite for core functionality.

### FR-17 Merchant Registration and Onboarding

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.3` |

**Requirement:** Merchants must be able to register on the platform and undergo a quality review process before their campaigns go live

**Source**

> “Merchant onboarding subject to quality review”
>
> — *Scope & Constraints - Constraints*

**Rationale:** Implied by the constraint that merchants must be onboarded with quality review. Must-Have as merchant participation is core to the platform.

### FR-18 Merchant Campaign Management

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.4` |

**Requirement:** Merchants must be able to create, configure, and manage offer campaigns including setting geofence areas, offer hours, and target audience parameters

**Source**

> “10,000 simultaneous merchant campaigns”
>
> — *Non-Functional Requirements - Scalability*

**Rationale:** The platform must support 10,000 campaigns — implying merchants can create and manage them. Must-Have as offers cannot exist without campaign management.

### FR-19 Payment Provider Integration

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.1` |

**Requirement:** The system must integrate with payment providers to securely access and analyze user transaction history for offer personalization

**Source**

> “Integrate with payment providers to analyze transaction history securely”
>
> — *Proposed Solution - Key Decisions*

**Rationale:** Key architectural decision and dependency. Must-Have as FR-03 (personalization) depends on this integration.

### FR-20 User Consent Management

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-01 (User Authentication & Registration) |
| Delivered by features | `F-01.4` |

**Requirement:** The system must implement transparent consent flows for location tracking and transaction data access, collecting explicit user consent before accessing any personal data

**Source**

> “User consent required for location and transaction data access”
>
> — *Scope & Constraints - Constraints*

**Rationale:** Regulatory requirement (GDPR/CCPA) and explicit constraint. Must-Have for compliance. Mitigation strategy also mentions 'Transparent consent flows'.

### FR-21 Geofence Zone Management

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.1` |

**Requirement:** The system must support creation, modification, and deletion of geofence zones associated with merchant locations, including defining radius/polygon boundaries and active hours

**Source**

> “Develop a mobile app that integrates geofencing, behavioral analytics, and transaction data to deliver targeted, real-time offers”
>
> — *Proposed Solution - Approach*

**Rationale:** Geofencing is the core delivery mechanism. FR-01 describes delivery but not the management of geofence zones themselves. Without this, offers cannot be spatially targeted. Must-Have as prerequisite to FR-01.

### FR-22 In-App Offer Feed

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.4` |

**Requirement:** Users must be able to browse available offers within the app, filtered by proximity, category, and relevance, without relying solely on push notifications

**Source**

> “Offers are delivered via push notifications or in-app, with seamless redemption at partner merchants”
>
> — *Proposed Solution - Approach*

**Rationale:** Source explicitly states 'or in-app' as a delivery channel alongside push. Users who disable push or have notification preferences set to in-app-only need a browsable offer feed. Must-Have as it's a stated delivery channel.

### FR-23 Offer Expiry and Lifecycle Management

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-04 (Offer Redemption & History) |
| Delivered by features | `F-04.4` |

**Requirement:** The system must automatically expire offers based on their validity period, remove expired offers from user views, and prevent redemption of expired offers

**Source**

> “As a User, Report an expired or invalid offer”
>
> — *User Stories - US8*

**Rationale:** US8 and AC6 reference expired offers, implying offers have lifecycles. Without automatic expiry management, users would constantly encounter stale offers. Must-Have for data integrity and UX.

### FR-24 User Profile and Preference Management

| Attribute | Value |
|---|---|
| Priority | Should-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-02 (Notification Infrastructure & User Preferences) |
| Delivered by features | `F-02.4` |

**Requirement:** Users must be able to view and update their profile information and set spending category preferences to improve offer relevance

**Source**

> “The offer matches the user's preferences and transaction history”
>
> — *Acceptance Criteria - AC1*

**Rationale:** AC1 references 'user's preferences' as a matching criterion. Users need a way to set/update these preferences. Should-Have as transaction history can bootstrap personalization without explicit preferences.

### FR-25 Offer Content Creation by Merchants

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-05 (Personalization Engine & Merchant Platform) |
| Delivered by features | `F-05.4` |

**Requirement:** Merchants must be able to create offer content including title, description, terms and conditions, images, discount value, validity period, and redemption limits

**Source**

> “Partner with merchants for exclusive, redeemable offers”
>
> — *Proposed Solution - Key Decisions*

**Rationale:** Offers must be created before they can be delivered. FR-18 covers campaign management (targeting, scheduling) but not the offer content itself. Must-Have as the platform cannot function without offer content.

### FR-26 Geofence Entry/Exit Event Detection

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | No |
| Classification | new |
| Delivered by epic | EP-03 (Geofencing & Real-Time Offer Delivery) |
| Delivered by features | `F-03.2` |

**Requirement:** The system must detect when a user enters or exits a geofenced area and trigger the offer matching pipeline within the 2-second delivery window

**Source**

> “A user with location tracking enabled enters a geofenced area during merchant offer hours, When The user is detected within the geofence”
>
> — *Acceptance Criteria - AC1*

**Rationale:** AC1 explicitly describes geofence entry detection as the trigger. This is the event-driven mechanism that connects location to offer delivery. Distinct from FR-21 (zone management) and FR-01 (delivery). Must-Have as the triggering mechanism.

### FR-27 Duplicate Redemption Prevention

| Attribute | Value |
|---|---|
| Priority | Must-Have |
| User-facing | Yes |
| Classification | new |
| Delivered by epic | EP-04 (Offer Redemption & History) |
| Delivered by features | `F-04.2` |

**Requirement:** The system must prevent users from redeeming the same offer more than once, displaying clear status when an offer has already been redeemed

**Source**

> “Tracks savings and avoids duplicate redemptions”
>
> — *User Stories - US4*

**Rationale:** US4 explicitly mentions avoiding duplicate redemptions. Without this, merchants face financial loss from multi-use of single-use offers. Must-Have for platform integrity.

---

## Non-functional requirements

| ID | Category | Title | Priority | Applies to epics |
|---|---|---|---|---|
| [NFR-01](#nfr-01-offer-notification-delivery-latency) | Performance | Offer Notification Delivery Latency | Must-Have | `EP-02`, `EP-03`, `EP-08` |
| [NFR-02](#nfr-02-app-screen-load-time) | Performance | App Screen Load Time | Must-Have | `EP-00`, `EP-03`, `EP-04`, `EP-05` |
| [NFR-03](#nfr-03-data-encryption-in-transit-and-at-rest) | Security | Data Encryption in Transit and at Rest | Must-Have | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07`, `EP-08` |
| [NFR-04](#nfr-04-gdpr-compliance) | Compliance | GDPR Compliance | Must-Have | `EP-01`, `EP-02`, `EP-05`, `EP-06`, `EP-07` |
| [NFR-05](#nfr-05-ccpa-compliance) | Compliance | CCPA Compliance | Must-Have | `EP-01`, `EP-05`, `EP-06` |
| [NFR-06](#nfr-06-concurrent-user-capacity) | Scalability | Concurrent User Capacity | Must-Have | `EP-03`, `EP-08` |
| [NFR-07](#nfr-07-simultaneous-campaign-capacity) | Scalability | Simultaneous Campaign Capacity | Must-Have | `EP-03`, `EP-05` |
| [NFR-08](#nfr-08-wcag-21-aa-compliance) | Accessibility | WCAG 2.1 AA Compliance | Must-Have | `EP-00`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07` |
| [NFR-09](#nfr-09-system-uptime) | Availability | System Uptime | Must-Have | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-08` |
| [NFR-10](#nfr-10-battery-efficiency) | Performance | Battery Efficiency | Should-Have | `EP-03`, `EP-06` |
| [NFR-11](#nfr-11-geofence-event-processing-throughput) | Scalability | Geofence Event Processing Throughput | Must-Have | `EP-03`, `EP-08` |
| [NFR-12](#nfr-12-location-data-minimization) | Security | Location Data Minimization | Must-Have | `EP-03`, `EP-05`, `EP-06` |

### NFR-01 Offer Notification Delivery Latency

| Attribute | Value |
|---|---|
| Category | Performance |
| Priority | Must-Have |
| Owning epic(s) | — (cross-cutting) |
| Features that implement it | — |
| Features it constrains | `F-02.1`, `F-02.3`, `F-03.2`, `F-03.3`, `F-08.2`, `F-08.5` |
| Applies to epics | `EP-02`, `EP-03`, `EP-08` |

**Requirement:** Offer notifications must be delivered within 2 seconds of a qualifying geofence event

**Implementation notes (from epics plan):** EP-02: notification service must deliver within its 500ms budget. EP-03: matching pipeline must complete within 1500ms. EP-08: caching and optimization ensure latency under load.

**Source**

> “Offer notifications must be delivered within 2 seconds of a qualifying event (e.g., entering a geofenced area)”
>
> — *Non-Functional Requirements - Performance*

**Rationale:** Explicit measurable target provided. Must-Have from 'must' language. Also a KPI target.

### NFR-02 App Screen Load Time

| Attribute | Value |
|---|---|
| Category | Performance |
| Priority | Must-Have |
| Owning epic(s) | — (cross-cutting) |
| Features that implement it | — |
| Features it constrains | `F-00.1`, `F-00.2`, `F-03.4`, `F-04.1`, `F-04.3`, `F-04.5`, `F-05.5`, `F-08.5` |
| Applies to epics | `EP-00`, `EP-03`, `EP-04`, `EP-05` |

**Requirement:** App screens must load within 1 second on devices from the last 3 years

**Implementation notes (from epics plan):** EP-00 establishes performant component patterns (lazy loading, virtualized lists). All user-facing screens (offer feed, history, merchant dashboard) must render within 1 second. Requires efficient API responses, pagination, and client-side caching.

**Source**

> “App screens must load within 1 second on devices from the last 3 years”
>
> — *Non-Functional Requirements - Performance*

**Rationale:** Explicit measurable target with device scope defined. Must-Have from 'must' language.

### NFR-03 Data Encryption in Transit and at Rest

| Attribute | Value |
|---|---|
| Category | Security |
| Priority | Must-Have |
| Owning epic(s) | EP-01 (User Authentication & Registration) |
| Features that implement it | — |
| Features it constrains | `F-01.1`, `F-01.2`, `F-01.3`, `F-01.5`, `F-04.1`, `F-05.1`, `F-05.3`, `F-08.4` |
| Applies to epics | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07`, `EP-08` |

**Requirement:** All user data must be encrypted in transit (TLS 1.2+) and at rest (AES-256 or equivalent)

**Implementation notes (from epics plan):** TLS 1.3 for all API communication. AES-256 for data at rest in Cloud SQL and BigQuery. Encryption keys managed via Cloud KMS. Applied universally across all services.

**Source**

> “All user data must be encrypted in transit and at rest”
>
> — *Non-Functional Requirements - Security*

**Rationale:** Explicit security requirement. Must-Have from 'must' language. Encryption standards inferred from industry best practice.

### NFR-04 GDPR Compliance

| Attribute | Value |
|---|---|
| Category | Compliance |
| Priority | Must-Have |
| Owning epic(s) | EP-01 (User Authentication & Registration) |
| Features that implement it | — |
| Features it constrains | `F-01.1`, `F-01.4`, `F-02.4`, `F-05.1`, `F-05.2`, `F-05.3`, `F-06.1`, `F-06.2`, `F-06.3`, `F-06.5`, `F-07.1` |
| Applies to epics | `EP-01`, `EP-02`, `EP-05`, `EP-06`, `EP-07` |

**Requirement:** The system must comply with GDPR for data privacy, user consent, right to erasure, and data portability

**Implementation notes (from epics plan):** EP-01: consent collection, lawful basis. EP-02: preference data handling. EP-05: transaction data processing (legitimate interest or consent). EP-06: data rights (erasure, portability, opt-out). EP-07: social sharing privacy.

**Source**

> “The app must comply with GDPR and CCPA for data privacy and user consent”
>
> — *Non-Functional Requirements - Security*

**Rationale:** Explicit regulatory requirement. Split from CCPA as they are distinct regulations with different obligations. Must-Have as regulatory compliance is mandatory.

### NFR-05 CCPA Compliance

| Attribute | Value |
|---|---|
| Category | Compliance |
| Priority | Must-Have |
| Owning epic(s) | EP-01 (User Authentication & Registration) |
| Features that implement it | — |
| Features it constrains | `F-01.4`, `F-06.2`, `F-06.3` |
| Applies to epics | `EP-01`, `EP-05`, `EP-06` |

**Requirement:** The system must comply with CCPA for California consumer data privacy rights including right to know, delete, and opt-out of sale

**Implementation notes (from epics plan):** EP-01: 'Do Not Sell' notice at registration. EP-05: transaction data qualifies as 'sale' under CCPA if shared with merchants. EP-06: right to delete, right to know.

**Source**

> “The app must comply with GDPR and CCPA for data privacy and user consent”
>
> — *Non-Functional Requirements - Security*

**Rationale:** Explicit regulatory requirement. Split from GDPR as a separate compliance obligation. Must-Have as regulatory compliance is mandatory.

### NFR-06 Concurrent User Capacity

| Attribute | Value |
|---|---|
| Category | Scalability |
| Priority | Must-Have |
| Owning epic(s) | EP-08 (Platform Resilience & Operational Excellence) |
| Features that implement it | `F-08.1`, `F-08.4` |
| Features it constrains | `F-02.3`, `F-03.3`, `F-03.5`, `F-04.2`, `F-08.2`, `F-08.3`, `F-08.6` |
| Applies to epics | `EP-03`, `EP-08` |

**Requirement:** The platform must support at least 1 million concurrent users without performance degradation

**Implementation notes (from epics plan):** EP-03: matching pipeline must handle event throughput from 1M users. EP-08: auto-scaling, load balancing, and caching to support concurrent connections.

**Source**

> “The platform must support at least 1 million concurrent users and 10,000 simultaneous merchant campaigns without degradation”
>
> — *Non-Functional Requirements - Scalability*

**Rationale:** Explicit scalability target. Split from campaign capacity as they are independently testable. Must-Have from 'must' language.

### NFR-07 Simultaneous Campaign Capacity

| Attribute | Value |
|---|---|
| Category | Scalability |
| Priority | Must-Have |
| Owning epic(s) | — (cross-cutting) |
| Features that implement it | — |
| Features it constrains | `F-03.1`, `F-05.4` |
| Applies to epics | `EP-03`, `EP-05` |

**Requirement:** The platform must support at least 10,000 simultaneous merchant campaigns without performance degradation

**Implementation notes (from epics plan):** EP-03: geofence matching must efficiently query across 10K active campaigns. EP-05: campaign management must support 10K concurrent campaigns without UI degradation.

**Source**

> “The platform must support at least 1 million concurrent users and 10,000 simultaneous merchant campaigns without degradation”
>
> — *Non-Functional Requirements - Scalability*

**Rationale:** Explicit scalability target for merchant side. Split from user capacity. Must-Have from 'must' language.

### NFR-08 WCAG 2.1 AA Compliance

| Attribute | Value |
|---|---|
| Category | Accessibility |
| Priority | Must-Have |
| Owning epic(s) | EP-00 (Design System & App Shell) |
| Features that implement it | `F-00.1`, `F-00.2` |
| Features it constrains | `F-00.1`, `F-00.2`, `F-02.2`, `F-02.4`, `F-02.5`, `F-03.4`, `F-04.3`, `F-05.5`, `F-06.4`, `F-07.2` |
| Applies to epics | `EP-00`, `EP-02`, `EP-03`, `EP-04`, `EP-05`, `EP-06`, `EP-07` |

**Requirement:** The app must meet WCAG 2.1 AA standards including support for screen readers, high-contrast modes, and adjustable font sizes

**Implementation notes (from epics plan):** EP-00 establishes accessibility foundations (touch targets, contrast, screen reader labels). All subsequent epics with user-facing screens inherit these patterns. Minimum touch targets 44x44pt. Color contrast ratio ≥4.5:1 for text.

**Source**

> “The app must meet WCAG 2.1 AA standards, including support for screen readers, high-contrast modes, and adjustable font sizes”
>
> — *Non-Functional Requirements - Accessibility*

**Rationale:** Explicit accessibility standard with specific features listed. Must-Have from 'must' language. Maps to AC8.

### NFR-09 System Uptime

| Attribute | Value |
|---|---|
| Category | Availability |
| Priority | Must-Have |
| Owning epic(s) | EP-08 (Platform Resilience & Operational Excellence) |
| Features that implement it | `F-08.2`, `F-08.3`, `F-08.6` |
| Features it constrains | `F-01.2`, `F-01.5`, `F-02.1`, `F-04.2`, `F-04.4`, `F-04.5`, `F-08.1`, `F-08.4` |
| Applies to epics | `EP-01`, `EP-02`, `EP-03`, `EP-04`, `EP-08` |

**Requirement:** The system must maintain 99.9% uptime with automated failover and recovery for critical services

**Implementation notes (from epics plan):** EP-08 is the primary implementation (failover, monitoring, DR). Other epics must implement health checks and graceful degradation. 99.9% = max 8.76 hours downtime/year.

**Source**

> “The system must maintain 99.9% uptime, with automated failover and recovery for critical services”
>
> — *Non-Functional Requirements - Reliability*

**Rationale:** Explicit availability target with recovery mechanism specified. Must-Have from 'must' language.

### NFR-10 Battery Efficiency

| Attribute | Value |
|---|---|
| Category | Performance |
| Priority | Should-Have |
| Owning epic(s) | — (cross-cutting) |
| Features that implement it | — |
| Features it constrains | `F-03.2`, `F-06.1`, `F-07.3` |
| Applies to epics | `EP-03`, `EP-06` |

**Requirement:** Geofencing logic must be optimized to minimize battery drain from continuous location tracking

**Implementation notes (from epics plan):** EP-03: optimize geofence monitoring (significant location changes, region rotation). EP-06: location tracking settings allow users to reduce battery impact by choosing 'app-open only'.

**Source**

> “Battery drain from continuous location tracking - Mitigation: Optimize geofencing logic, allow user control over tracking frequency”
>
> — *Dependencies & Risks*

**Rationale:** Identified as a medium-likelihood risk with explicit mitigation strategy. Should-Have as it's a mitigation rather than a hard requirement. No specific target defined — flagged in gaps.

### NFR-11 Geofence Event Processing Throughput

| Attribute | Value |
|---|---|
| Category | Scalability |
| Priority | Must-Have |
| Owning epic(s) | EP-08 (Platform Resilience & Operational Excellence) |
| Features that implement it | `F-08.5` |
| Features it constrains | `F-03.1`, `F-03.3`, `F-04.4`, `F-05.2`, `F-08.1`, `F-08.3` |
| Applies to epics | `EP-03`, `EP-08` |

**Requirement:** The system must process geofence entry/exit events at a rate sufficient to serve 1 million concurrent users, each potentially triggering multiple geofence events per hour, without exceeding the 2-second delivery SLA

**Implementation notes (from epics plan):** EP-03: pipeline architecture must support high throughput. EP-08: auto-scaling and caching ensure throughput under peak load.

**Source**

> “The platform must support at least 1 million concurrent users... Offer notifications must be delivered within 2 seconds”
>
> — *Non-Functional Requirements - Scalability and Performance*

**Rationale:** Combining NFR-01 (2s latency) with NFR-06 (1M users) implies a massive event processing throughput requirement. This is a distinct scalability concern from user connections or campaign count.

### NFR-12 Location Data Minimization

| Attribute | Value |
|---|---|
| Category | Security |
| Priority | Must-Have |
| Owning epic(s) | — (cross-cutting) |
| Features that implement it | — |
| Features it constrains | `F-05.1`, `F-05.2`, `F-06.2` |
| Applies to epics | `EP-03`, `EP-05`, `EP-06` |

**Requirement:** The system must implement data minimization principles — collecting only the location precision necessary for geofence matching and not storing continuous location trails

**Implementation notes (from epics plan):** EP-03: only store geofence events (not continuous location). EP-05: spending profiles use aggregates, not raw data. EP-06: deletion removes all location-derived data.

**Source**

> “The app must comply with GDPR... Utilize real-time location tracking with user consent for precise offer delivery”
>
> — *Non-Functional Requirements - Security; Proposed Solution - Key Decisions*

**Rationale:** GDPR Article 5(1)(c) requires data minimization. Continuous location storage would be disproportionate to the purpose of offer delivery. Must-Have for GDPR compliance.

---

## Constraints

| ID | Type | Constraint |
|---|---|---|
| CON-01 | Technology | Mobile app must be built for iOS and Android platforms |
| CON-02 | Regulatory | User consent is required before accessing location and transaction data |
| CON-03 | Organisational | Initial launch limited to select urban markets only |
| CON-04 | Organisational | Merchant onboarding is subject to quality review before campaigns go live |
| CON-05 | Technology | Push notifications must use APNs (iOS) and FCM (Android) |
| CON-06 | Technology | No web-based consumer interface — mobile app only |

### CON-01 (Technology)

**Constraint:** Mobile app must be built for iOS and Android platforms

**Source**

> “Mobile app for iOS and Android”
>
> — *Scope & Constraints - In Scope*

**Rationale:** Fixed platform decision — not negotiable. Constrains technology choices to cross-platform or dual native development.

### CON-02 (Regulatory)

**Constraint:** User consent is required before accessing location and transaction data

**Source**

> “User consent required for location and transaction data access”
>
> — *Scope & Constraints - Constraints*

**Rationale:** Regulatory constraint from GDPR/CCPA. Fixed decision that affects all data collection flows.

### CON-03 (Organisational)

**Constraint:** Initial launch limited to select urban markets only

**Source**

> “Initial launch limited to select urban markets”
>
> — *Scope & Constraints - Constraints*

**Rationale:** Business decision constraining geographic scope. Affects geofence coverage and merchant partnerships.

### CON-04 (Organisational)

**Constraint:** Merchant onboarding is subject to quality review before campaigns go live

**Source**

> “Merchant onboarding subject to quality review”
>
> — *Scope & Constraints - Constraints*

**Rationale:** Operational constraint affecting merchant go-live timelines. Fixed process decision.

### CON-05 (Technology)

**Constraint:** Push notifications must use APNs (iOS) and FCM (Android)

**Source**

> “Push notification services (APNs, FCM)”
>
> — *Dependencies & Risks - Dependencies*

**Rationale:** Technology dependency that constrains notification implementation. These are the only viable push services for mobile platforms.

### CON-06 (Technology)

**Constraint:** No web-based consumer interface — mobile app only

**Source**

> “Web-based user interface for consumers”
>
> — *Scope & Constraints - Out of Scope*

**Rationale:** Explicit scope exclusion. Constrains delivery to mobile channels only for consumers.

---

## Assumptions

| ID | Assumption | Needs confirmation | Rationale and risk if wrong |
|---|---|---|---|
| ASM-01 | The merchant analytics dashboard will be a web application (not mobile), as it is targeted at marketing managers who work from desktops | ⚠️ Yes | Source says consumer web UI is out of scope but does not specify the merchant dashboard platform. Marketing managers typically use desktop tools. If wrong, merchant dashboard would need to be mobile-native too. |
| ASM-02 | Payment provider integration will use Open Banking APIs or similar standardized interfaces rather than direct bank integrations | ⚠️ Yes | Source mentions 'integration with payment providers' but does not specify the mechanism. Open Banking is the standard approach in UK/EU. If wrong, custom integrations per provider would significantly increase scope. |
| ASM-03 | The 30-day data deletion window (from AC10) applies to all user data including backups and analytics aggregates | ⚠️ Yes | AC10 states 'All user data is permanently deleted from the system within 30 days' but does not clarify if aggregated/anonymized analytics data is included. GDPR allows retention of anonymized data. |
| ASM-04 | Geofencing will use a combination of GPS, Wi-Fi, and cell tower triangulation for accuracy in urban environments | ⚠️ Yes | Source mentions 'Mobile OS location services APIs' as a dependency but does not specify accuracy requirements. Urban environments may need multiple signals for reliable geofence triggering. |
| ASM-05 | The platform will operate in English language only for initial launch | ⚠️ Yes | No mention of internationalization or multi-language support in the source. Urban markets could include non-English-speaking populations. If wrong, i18n adds significant scope. |
| ASM-06 | Offer redemption will use a QR code or unique code mechanism presented on the user's device and scanned/entered by the merchant | ⚠️ Yes | Source says 'redeem offers instantly via a mobile interface' but does not specify the redemption mechanism. QR/code is the most common pattern. If wrong (e.g., NFC, automatic via payment), architecture changes significantly. |
| ASM-07 | Cloud-based infrastructure will be used (AWS, GCP, or Azure) with auto-scaling capabilities | ⚠️ Yes | Risk mitigation mentions 'Cloud-based infrastructure with auto-scaling' but does not specify provider. This is assumed as the deployment model. If on-premise is required, architecture changes fundamentally. |
| ASM-08 | The 100,000 MAU target within 12 months implies a phased rollout starting with a smaller beta cohort | ⚠️ Yes | KPI target is 100K MAU in 12 months from 0 baseline. This implies a growth curve, not a big-bang launch. Phased rollout affects infrastructure sizing and merchant onboarding pace. |
| ASM-09 | Merchants will pay for the platform via a subscription or per-campaign fee model | ⚠️ Yes | Source mentions merchants 'willing to pay for premium placement' but does not define the business model. Revenue model affects merchant-facing features (billing, tiers, etc.). |
| ASM-10 | User authentication will use email/phone + password with optional biometric (Face ID/Touch ID) for convenience | ⚠️ Yes | Source implies authentication exists (AC4: 'logged in') but never specifies the method. Email/phone + biometric is standard for consumer mobile apps. If SSO or social login is required, scope increases. |

---

## Gaps & open questions

These are areas the source brief leaves unspecified. Each one blocks some design work until a stakeholder answers the suggested question.

### GAP-01: No authentication or authorization approach specified for users or merchants

- **Impact:** Cannot design login flow, session management, role-based access, or security architecture
- **Question to ask:** What authentication methods should be supported? (email/password, social login, biometric, SSO for merchants?)
- **Rationale:** Source implies users are 'logged in' (AC4) and merchants 'log into the dashboard' (AC9) but never defines how. Authentication is foundational to all personalized features.

### GAP-02: No offer redemption mechanism specified

- **Impact:** Cannot design the merchant-side redemption flow or POS integration
- **Question to ask:** How will offers be redeemed? QR code scan, unique code entry, NFC tap, or automatic via linked payment card?
- **Rationale:** FR4 says 'redeem offers instantly via a mobile interface' but the actual mechanism (QR, code, NFC, payment-linked) is undefined. This affects both mobile app UX and merchant integration.

### GAP-03: No data retention policy defined beyond the 30-day deletion window

- **Impact:** Cannot design data lifecycle management, archival strategy, or compliance reporting
- **Question to ask:** What is the data retention policy for transaction history, offer history, and analytics data? How long should data be kept for active users?
- **Rationale:** GDPR requires defined retention periods. Source only mentions deletion on request (30 days) but not routine retention limits for active accounts.

### GAP-04: No offline behavior specified for the mobile app

- **Impact:** Cannot design caching strategy, offline offer display, or sync mechanisms
- **Question to ask:** How should the app behave when the user has no network connectivity? Should cached offers be viewable? Should redemption work offline?
- **Rationale:** Mobile apps commonly face connectivity issues. Source does not address offline scenarios, which affects UX and architecture (local storage, sync queues).

### GAP-05: No error handling or fallback behavior defined for failed geofence detection or notification delivery

- **Impact:** Cannot design retry logic, fallback notification channels, or user communication for service degradation
- **Question to ask:** What should happen if a geofence event is missed or a notification fails to deliver? Should there be retry logic, in-app fallback, or email/SMS backup?
- **Rationale:** Real-time geofencing can fail due to device settings, battery optimization, or network issues. No fallback strategy is defined.

### GAP-06: No battery drain target or acceptable threshold specified

- **Impact:** Cannot set acceptance criteria for battery optimization or test against a measurable standard
- **Question to ask:** What is the acceptable battery impact? (e.g., <5% additional drain per day with always-on tracking)
- **Rationale:** NFR-10 identifies battery optimization as needed but provides no measurable target. Cannot verify compliance without a threshold.

### GAP-07: No merchant POS integration requirements specified

- **Impact:** Cannot design the merchant-side technical integration for offer redemption confirmation
- **Question to ask:** How will merchants confirm redemptions? Via a separate merchant app, existing POS integration, or manual confirmation on a web dashboard?
- **Rationale:** AC3 states 'the merchant receives confirmation of redemption' but does not specify the channel or integration point.

### GAP-08: No specific urban markets identified for initial launch

- **Impact:** Cannot size geofence infrastructure, plan merchant partnerships, or estimate initial load
- **Question to ask:** Which cities/markets are targeted for initial launch? How many merchants per market?
- **Rationale:** CON-03 constrains launch to 'select urban markets' but does not name them. Market selection affects infrastructure region, merchant onboarding, and regulatory considerations.

### GAP-09: No merchant billing or revenue model defined

- **Impact:** Cannot design merchant billing features, pricing tiers, or payment collection
- **Question to ask:** What is the merchant pricing model? Subscription, per-campaign, per-redemption, or commission-based?
- **Rationale:** Source mentions merchants 'willing to pay for premium placement' but defines no pricing structure. This affects merchant dashboard features and backend billing systems.

### GAP-10: No user onboarding or tutorial flow specified

- **Impact:** Cannot design first-time user experience or consent collection sequence
- **Question to ask:** What should the first-time user experience look like? What permissions are requested at what point in the onboarding?
- **Rationale:** Location and data permissions require careful UX to maximize opt-in rates (identified as a high-impact risk). No onboarding flow is defined.

### GAP-11: No API rate limiting or abuse prevention strategy defined

- **Impact:** Cannot protect the platform from malicious actors or ensure fair usage
- **Question to ask:** What rate limits should apply to API consumers? What abuse scenarios need to be mitigated (fake redemptions, location spoofing)?
- **Rationale:** Location-based systems are vulnerable to GPS spoofing and fraudulent redemptions. No anti-abuse measures are mentioned.

### GAP-12: No monitoring, alerting, or observability requirements specified

- **Impact:** Cannot design operational dashboards, incident response, or SLA monitoring
- **Question to ask:** What monitoring and alerting is required? What are the escalation paths for service degradation?
- **Rationale:** 99.9% uptime (NFR-09) requires robust monitoring to detect and respond to incidents. No observability strategy is defined.

### GAP-13: No push notification permission denial handling specified

- **Impact:** Cannot design fallback UX for users who deny push permissions on iOS/Android
- **Question to ask:** What should happen if a user denies push notification permission? Should the app still function with in-app-only offers, or is push required for core value?
- **Rationale:** iOS requires explicit push permission. Denial rates can be 40-60%. Without a fallback strategy, a large portion of users may get no offers at all.

### GAP-14: No offer matching/ranking algorithm defined

- **Impact:** Cannot design the personalization engine that selects which offers to show each user
- **Question to ask:** How should offers be ranked and matched to users? Rules-based (category matching), ML-based (collaborative filtering), or hybrid? What signals beyond transaction history should be used?
- **Rationale:** FR-03 says 'analyze transaction history to personalize' but does not define the algorithm. This is the core differentiator of the platform and affects architecture (ML pipeline vs rules engine).

### GAP-15: No testing or QA strategy for location-based features specified

- **Impact:** Cannot validate geofence accuracy, notification timing, or location-dependent behavior in CI/CD
- **Question to ask:** How should location-based features be tested? Simulated locations in CI, field testing in target markets, or both?
- **Rationale:** Location-based features are notoriously difficult to test in automated environments. Without a strategy, quality assurance for the core feature is undefined.

### GAP-16: No multi-tenancy or white-label requirements specified for merchant isolation

- **Impact:** Cannot determine if merchants see each other's data, if campaigns are isolated, or if the platform could be white-labeled
- **Question to ask:** Should merchants be fully isolated from each other? Can a merchant see competitor campaigns in the same area? Is white-labeling a future consideration?
- **Rationale:** With 10,000 simultaneous campaigns, data isolation between competing merchants is critical for trust. Source does not address multi-tenancy boundaries.

### Question checklist

- [ ] **GAP-01:** What authentication methods should be supported? (email/password, social login, biometric, SSO for merchants?)
- [ ] **GAP-02:** How will offers be redeemed? QR code scan, unique code entry, NFC tap, or automatic via linked payment card?
- [ ] **GAP-03:** What is the data retention policy for transaction history, offer history, and analytics data? How long should data be kept for active users?
- [ ] **GAP-04:** How should the app behave when the user has no network connectivity? Should cached offers be viewable? Should redemption work offline?
- [ ] **GAP-05:** What should happen if a geofence event is missed or a notification fails to deliver? Should there be retry logic, in-app fallback, or email/SMS backup?
- [ ] **GAP-06:** What is the acceptable battery impact? (e.g., <5% additional drain per day with always-on tracking)
- [ ] **GAP-07:** How will merchants confirm redemptions? Via a separate merchant app, existing POS integration, or manual confirmation on a web dashboard?
- [ ] **GAP-08:** Which cities/markets are targeted for initial launch? How many merchants per market?
- [ ] **GAP-09:** What is the merchant pricing model? Subscription, per-campaign, per-redemption, or commission-based?
- [ ] **GAP-10:** What should the first-time user experience look like? What permissions are requested at what point in the onboarding?
- [ ] **GAP-11:** What rate limits should apply to API consumers? What abuse scenarios need to be mitigated (fake redemptions, location spoofing)?
- [ ] **GAP-12:** What monitoring and alerting is required? What are the escalation paths for service degradation?
- [ ] **GAP-13:** What should happen if a user denies push notification permission? Should the app still function with in-app-only offers, or is push required for core value?
- [ ] **GAP-14:** How should offers be ranked and matched to users? Rules-based (category matching), ML-based (collaborative filtering), or hybrid? What signals beyond transaction history should be used?
- [ ] **GAP-15:** How should location-based features be tested? Simulated locations in CI, field testing in target markets, or both?
- [ ] **GAP-16:** Should merchants be fully isolated from each other? Can a merchant see competitor campaigns in the same area? Is white-labeling a future consideration?
