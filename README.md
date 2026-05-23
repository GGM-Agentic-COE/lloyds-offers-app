# Smart Location-Based Offers Platform

**Lloyds Banking Group** · Personalised, real-time offers delivered to customers based on their location, spending patterns, and preferences.

---

## What is this?

A mobile-first platform that connects consumers with nearby merchant offers using geofencing, behavioural analytics, and real-time push notifications. Think of it as a smart loyalty programme that knows where you are and what you like — delivering the right offer at the right time.

### Key Capabilities

- 📍 **Geofenced offer delivery** — push notifications when you walk near a participating merchant
- 🧠 **Personalised matching** — offers ranked by your spending habits and category preferences
- 📱 **QR code redemption** — instant redemption at the till with a time-limited QR code
- 📊 **Merchant analytics** — campaign performance dashboard for merchants
- 🔒 **Privacy-first** — granular consent controls, GDPR-compliant data handling

---

## Demo Scenarios

The app uses **Mock Service Worker (MSW)** to simulate all backend APIs. Different email addresses trigger different user personas and scenarios:

| Email | Scenario | What you'll see |
|---|---|---|
| `happy@example.com` | ✅ Happy path | 3 offers, redeemed history (£16.90 saved), expired offers |
| `onthemove@example.com` | 📍 Dynamic location | Offers change every 15s as you "walk" through London. Push notifications appear for each new area |
| `foodie@example.com` | 🍽️ Dining enthusiast | 5 restaurant offers (Wagamama, Dishoom, Nando's, Pret, The Ivy). Heavy redemption history (£37 saved) |
| `shopper@example.com` | 🛍️ Retail lover | 4 fashion/beauty offers (Zara, John Lewis, H&M, Boots). £42.50 saved |
| `traveler@example.com` | 🌍 Unsupported city | Empty offers feed — "Coming soon to your area" |
| `network@example.com` | ⚠️ Network errors | Simulates timeouts and connectivity failures |
| Any other email | Default happy path | Same as `happy@example.com` |

**OTP code for all scenarios:** `123456`

### The "On The Move" Demo (Recommended)

Register with `onthemove@example.com` to see the full real-time experience:

1. Every **15 seconds**, your simulated location changes
2. A **push notification** slides down showing the best offer at your new location
3. The **offers feed** updates simultaneously with all nearby offers
4. The **green hero card** shows your current area

**Simulated route through London:**

```
Oxford Circus → Soho → Covent Garden → South Bank → Borough Market → (loops)
   (retail)     (food/nightlife)  (theatre/beauty)  (riverside/culture)  (artisan food)
```

---

## Installation & Running

### Prerequisites

- Node.js 18+ 
- npm 9+

### Quick Start

```bash
# Navigate to the consumer app
cd ui/consumer-app

# Install dependencies
npm install

# Start the development server
npx vite --port 3000 --open
```

The app opens at **http://localhost:3000**

### What happens on launch

1. MSW (Mock Service Worker) starts and intercepts all API calls
2. The app auto-authenticates in dev mode (skips login for quick testing)
3. You land on the **Offers** tab with live data from mocks
4. Push notifications start appearing after 15 seconds

### To test the full onboarding flow

Clear localStorage to reset auth state:
```javascript
// In browser console:
localStorage.clear();
location.reload();
```

Then register with any of the scenario emails above.

---

## App Screens & Navigation

### Onboarding Flow (6 screens)
```
Register → OTP Verify → Biometric → Location Consent → Transaction Consent → Push Permission → Main App
```

### Main App (4 tabs)
| Tab | Screen | Features |
|---|---|---|
| 🏷️ Offers | Feed + Detail + Redeem | Nearby offers, tap to view, QR redemption |
| 🕐 History | Redeemed + Expired | Savings total, redemption dates, expired offers |
| 👤 Profile | Preferences | Spending categories, budget sensitivity, radius |
| ⚙️ Settings | Privacy & Account | Notifications, location, transaction data, export, delete |

### Additional Screens
| Route | Purpose |
|---|---|
| `/offers/:id` | Offer detail (terms, merchant, rating, redeem button) |
| `/offers/:id/redeem` | QR code + manual code + countdown timer |
| `/offers/:id/rate` | Post-redemption star rating |
| `/offers/unsupported` | "Coming soon to your area" |
| `/settings/notifications` | Delivery mode, quiet hours, category toggles |
| `/settings/location` | Location tracking preference |
| `/settings/transaction` | Transaction data opt-out |
| `/settings/export` | GDPR data export |
| `/settings/delete` | Account deletion (7-day cooling-off) |

---

## Project Structure

```
offers/
├── ui/
│   ├── consumer-app/          ← React web app (this demo)
│   │   ├── src/
│   │   │   ├── components/    Atoms (Button, Input)
│   │   │   ├── pages/         All screen components
│   │   │   ├── stores/        Zustand (auth, location)
│   │   │   ├── services/      Axios API client
│   │   │   ├── mocks/         MSW handlers (scenario-driven)
│   │   │   └── App.tsx        Router + AuthGuard + AppShell
│   │   └── package.json
│   └── merchant-dashboard/    React web dashboard (scaffold)
├── services/                  6 Spring Boot microservices
│   ├── auth-service/
│   ├── user-service/
│   ├── notification-service/
│   ├── offer-service/
│   ├── geofence-service/
│   └── merchant-service/
├── api-specs/                 OpenAPI 3.0.3 specs (6 files)
├── architecture/              Solution, Integration, LLD docs
├── wireframes/                HTML wireframes + user flows
├── stories/                   User stories (9 epic files, 72 stories)
├── tests/                     Manual + automated test cases
└── kb-L3-offers-application-baseline.md
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, TypeScript 5, Vite 5, Tailwind CSS |
| State | Zustand (client), TanStack Query (server) |
| Forms | React Hook Form + Zod |
| API Mocking | MSW 2.4 (Mock Service Worker) |
| Backend | Java 21, Spring Boot 3.3, PostgreSQL, Redis |
| Messaging | Google Cloud Pub/Sub |
| Geospatial | PostGIS |
| Cloud | GCP (GKE, Cloud SQL, Memorystore, BigQuery) |
| Design System | Lloyds Banking Group official tokens |

---

## Design System

Based on the **Lloyds Banking Group Digital Design System**:

- **Primary colour:** Lloyds Green `#006A4D`
- **Typography:** System font stack (GT Ultra in production)
- **Buttons:** 52px height, 8px radius, primary/secondary/ghost/destructive
- **Cards:** 12px radius, 1px border, subtle shadow
- **Touch targets:** Minimum 44×44px
- **Accessibility:** WCAG 2.1 AA compliant

---

## Architecture Overview

```
Consumer App ──→ API Gateway (Apigee) ──→ Microservices ──→ Cloud SQL / Redis / Pub/Sub
                                              │
Merchant Dashboard ─────────────────────────────┘

6 microservices:
  auth-service         → Registration, OTP, JWT sessions, consent
  user-service         → Profile, preferences, deletion, export
  notification-service → Push delivery (APNs/FCM), frequency capping
  offer-service        → Offers, redemption, history, ratings
  geofence-service     → Zones, event processing, city boundaries
  merchant-service     → Registration, campaigns, analytics
```

---

## Delivery Plan

| Sprint | Focus | Epics |
|---|---|---|
| 1 | Foundation | Auth, Notifications, Design System |
| 2 | Core MVP | Geofencing, Offer Delivery, Redemption |
| 3 | Intelligence | Personalization, Merchant Platform, Privacy |
| 4 | Scale | Social Sharing, Wearables, Resilience |

**Total:** 9 epics, 41 features, 72 stories, 249 story points

---

## Running Backend Services (Optional)

Each service can be run independently with Docker:

```bash
cd services/auth-service
mvn clean package -DskipTests
docker build -t auth-service .
docker run -p 8080:8080 auth-service
```

Or use the consumer app with MSW mocks (no backend needed).

---

## Key Files

| File | Purpose |
|---|---|
| `ui/consumer-app/src/mocks/handlers.ts` | All mock API responses + scenarios |
| `ui/consumer-app/src/stores/locationStore.ts` | Simulated location changes |
| `ui/consumer-app/src/App.tsx` | All routes + auth guard |
| `api-specs/*.yaml` | OpenAPI specs for all 6 services |
| `architecture/lld.md` | Database schemas + handler logic |
| `wireframes/sprint1-wireframes.html` | Visual wireframes (open in browser) |
| `wireframes/user-flows.md` | All 14 user journey flows |

---

*Built with ❤️ for Lloyds Banking Group · Powered by Ascendion · Engineering to the Power of AI™*
