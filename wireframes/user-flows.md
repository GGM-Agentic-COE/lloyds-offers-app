# Smart Location-Based Offers Platform — User Flows
**Sprint 1–4 · All Journeys**

---

## Flow 1: New User Onboarding

```
START
  │
  ▼
┌──────────────────┐
│  Registration    │  /onboarding/register
│  Enter phone/email│
└────────┬─────────┘
         │
    ┌────┴────┐
    │ Valid?  │
    └────┬────┘
     No  │  Yes
     │   │
     ▼   ▼
  [Error: ┌──────────────────┐
  invalid │  Check duplicate │
  format] └────────┬─────────┘
                   │
              ┌────┴────┐
              │Exists?  │
              └────┬────┘
           Yes │   │ No
               │   │
               ▼   ▼
         [Error:  ┌──────────────────┐
         "Account │  Send OTP        │
         exists"] │  (SMS or Email)  │
                  └────────┬─────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │  OTP Entry       │  /onboarding/verify
                  │  6-digit code    │
                  │  90s timer       │
                  └────────┬─────────┘
                           │
                      ┌────┴────┐
                      │Correct? │
                      └────┬────┘
                   No  │   │ Yes
                       │   │
                  ┌────┴───┴────┐
                  │             │
                  ▼             ▼
           ┌───────────┐  ┌──────────────────┐
           │ Attempts  │  │  Account Verified│
           │ < 5?      │  └────────┬─────────┘
           └─────┬─────┘           │
            No   │ Yes             ▼
             │   │        ┌──────────────────┐
             ▼   ▼        │  Biometric       │  /onboarding/biometric
        [30-min  [Show    │  Enable/Skip     │
        lockout] error]   └────────┬─────────┘
                                   │
                          ┌────────┴────────┐
                          │                 │
                     [Enable]          [Skip]
                          │                 │
                          ▼                 ▼
                   [OS Biometric     [Password login
                    prompt]           for future]
                          │                 │
                          └────────┬────────┘
                                   │
                                   ▼
                          ┌──────────────────┐
                          │  Location Consent│  /onboarding/consent-location
                          │  Always / App /  │
                          │  Not now         │
                          └────────┬─────────┘
                                   │
                          ┌────────┴────────────────┐
                          │                         │
                    [Always/App]              [Not now]
                          │                         │
                          ▼                         │
                   [OS Location                     │
                    Permission Dialog]              │
                          │                         │
                     ┌────┴────┐                    │
                     │Granted? │                    │
                     └────┬────┘                    │
                  No  │   │ Yes                     │
                      │   │                         │
                      ▼   └─────────┬───────────────┘
                [Fallback            │
                 to "Not now"]       ▼
                          ┌──────────────────┐
                          │Transaction Consent│  /onboarding/consent-transaction
                          │  Allow / No thanks│
                          └────────┬─────────┘
                                   │
                          ┌────────┴────────┐
                          │                 │
                     [Allow]          [No thanks]
                          │                 │
                          ▼                 ▼
                   [Personalized      [Category-only
                    offers]            matching]
                          │                 │
                          └────────┬────────┘
                                   │
                                   ▼
                          ┌──────────────────┐
                          │  Push Permission │  /onboarding/push-permission
                          │  Enable / Later  │
                          └────────┬─────────┘
                                   │
                          ┌────────┴────────┐
                          │                 │
                     [Enable]          [Later]
                          │                 │
                          ▼                 │
                   [OS Push Dialog]         │
                          │                 │
                     ┌────┴────┐            │
                     │Granted? │            │
                     └────┬────┘            │
                  No  │   │ Yes             │
                      │   │                 │
                      ▼   └─────────┬───────┘
                [Show info           │
                 banner:             │
                 "Notifs off"]       │
                      │              │
                      └──────┬───────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │  MAIN APP        │  /offers
                    │  (Onboarding     │
                    │   Complete!)     │
                    └──────────────────┘
```

---

## Flow 2: Offer Discovery & Engagement

```
┌─────────────────────────────────────────────────────────────────────────┐
│  TRIGGER: User enters geofenced area near merchant                      │
└────────────────────────────────┬────────────────────────────────────────┘
                                 │
                                 ▼
                    ┌──────────────────────┐
                    │  Matching Pipeline   │  (Backend, <2s)
                    │  • Check active hours│
                    │  • Filter preferences│
                    │  • Frequency cap     │
                    │  • Rank by relevance │
                    └────────────┬─────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
              [Cap reached /              [Offer matched]
               No match]                      │
                    │                         ▼
                    ▼              ┌──────────────────────┐
              [No action]         │  Push Notification   │  (Lock screen)
                                  │  "🍕 20% off near   │
                                  │   you!"              │
                                  └────────────┬─────────┘
                                               │
                                  ┌────────────┴────────────┐
                                  │                         │
                            [Dismiss]                  [Tap]
                                  │                         │
                                  ▼                         ▼
                            [No action]         ┌──────────────────────┐
                                                │  Offer Detail        │  /offers/:id
                                                │  • Image             │
                                                │  • Discount badge    │
                                                │  • Terms             │
                                                │  • Merchant address  │
                                                │  • Distance          │
                                                └────────────┬─────────┘
                                                             │
                                                ┌────────────┴────────────┐
                                                │            │            │
                                          [Redeem]     [Share]      [Back]
                                                │            │            │
                                                ▼            ▼            ▼
                                          ┌──────────┐ [Native     [Offer
                                          │ QR Code  │  Share       Feed]
                                          │ Screen   │  Sheet]
                                          └──────────┘

─── ALTERNATIVE ENTRY: User opens app manually ───

┌──────────────────┐
│  Open App        │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Offers Tab      │  /offers
│  (Feed view)     │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
[Offers exist]          [No offers]
    │                         │
    ▼                         ▼
┌──────────────────┐  ┌──────────────────┐
│  Scrollable Feed │  │  Empty State     │
│  • Sort: nearest │  │  "No offers yet" │
│  • Filter: cats  │  │  "Check back     │
│  • Pull refresh  │  │   soon!"         │
└────────┬─────────┘  └──────────────────┘
         │
    [Tap offer card]
         │
         ▼
┌──────────────────┐
│  Offer Detail    │  /offers/:id
└──────────────────┘
```

---

## Flow 3: Offer Redemption

```
┌──────────────────┐
│  Offer Detail    │  /offers/:id
│  [Redeem button] │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  QR Code Screen  │  /offers/:id/redeem
│  • Unique token  │
│  • 5-min expiry  │
│  • Full bright   │
│  • Countdown     │
│  • Manual code   │
│    below QR      │
└────────┬─────────┘
         │
    ┌────┴────────────────────────────────┐
    │                │                    │
[Merchant       [Timer expires]     [Already
 scans QR]           │               redeemed]
    │                ▼                    │
    ▼          ┌──────────────┐           ▼
┌──────────┐   │ "Code expired"│    ┌──────────────┐
│ Validate │   │ [Regenerate]  │    │ "Already     │
│ server   │   └──────────────┘    │  used"       │
└────┬─────┘                        └──────────────┘
     │
┌────┴────┐
│ Valid?  │
└────┬────┘
 No  │  Yes
 │   │
 ▼   ▼
[Error:  ┌──────────────────┐
wrong    │  Merchant Confirm│  (Merchant app)
merchant │  [Confirm button]│
/expired]└────────┬─────────┘
                  │
                  ▼
         ┌──────────────────┐
         │  Success!        │  (Both screens)
         │  Customer: 🎉    │
         │  "Offer Redeemed"│
         │  "You saved £X"  │
         │                  │
         │  Merchant:       │
         │  "Redemption     │
         │   confirmed"     │
         └────────┬─────────┘
                  │
                  ▼
         ┌──────────────────┐
         │  History Tab     │  /history
         │  (Offer appears  │
         │   as "Redeemed") │
         └──────────────────┘
```

---

## Flow 4: Session Lifecycle & Authentication

```
┌──────────────────┐
│  App Launch      │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Check stored    │
│  tokens          │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
[Tokens exist]          [No tokens]
    │                         │
    ▼                         ▼
┌──────────────────┐  ┌──────────────────┐
│  Access token    │  │  Registration    │  /onboarding/register
│  valid?          │  │  (First time)    │
└────────┬─────────┘  └──────────────────┘
         │
    ┌────┴────────────────────┐
    │                         │
  [Valid]                [Expired]
    │                         │
    ▼                         ▼
┌──────────────────┐  ┌──────────────────┐
│  MAIN APP        │  │  Refresh token   │
│  (Immediate)     │  │  valid?          │
└──────────────────┘  └────────┬─────────┘
                               │
                      ┌────────┴────────┐
                      │                 │
                 [Valid]           [Expired]
                      │                 │
                      ▼                 ▼
               ┌──────────────┐  ┌──────────────────┐
               │Silent Refresh│  │  Biometric       │
               │(Background)  │  │  Prompt          │
               └──────┬───────┘  └────────┬─────────┘
                      │                    │
                      │               ┌────┴────┐
                      │               │Success? │
                      │               └────┬────┘
                      │            No  │   │ Yes
                      │                │   │
                      │                ▼   │
                      │          ┌─────────┴──┐
                      │          │ Password   │
                      │          │ Login      │
                      │          └─────┬──────┘
                      │                │
                      └────────┬───────┘
                               │
                               ▼
                      ┌──────────────────┐
                      │  MAIN APP        │
                      │  (New tokens)    │
                      └──────────────────┘

─── LOGOUT ───

┌──────────────────┐       ┌──────────────────┐       ┌──────────────────┐
│  Tap "Sign out"  │──────▶│  Invalidate      │──────▶│  Clear secure    │
│  in Settings     │       │  device tokens   │       │  storage         │
└──────────────────┘       │  (server-side)   │       └────────┬─────────┘
                           └──────────────────┘                │
                                                               ▼
                                                      ┌──────────────────┐
                                                      │  Registration    │
                                                      │  /onboarding     │
                                                      └──────────────────┘
```

---

## Flow 5: Profile & Preference Management

```
┌──────────────────┐
│  Profile Tab     │  /profile
│  • Display name  │
│  • Categories    │
│  • Budget        │
│  • Radius        │
└────────┬─────────┘
         │
    ┌────┴────────────────────────────────────┐
    │                    │                    │
[Edit categories]  [Change budget]     [Change radius]
    │                    │                    │
    ▼                    ▼                    ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ Toggle chips │  │ Select:      │  │ Select:      │
│ (multi-sel)  │  │ Low/Med/High │  │ 500m/1km/    │
│ Auto-save    │  │ Auto-save    │  │ 2km/5km      │
└──────────────┘  └──────────────┘  │ Auto-save    │
                                    └──────────────┘
         │
         ▼
[Offer feed updates to reflect new preferences on next refresh]
```

---

## Flow 6: Notification Preference Management

```
┌──────────────────┐
│  Settings Tab    │  /settings
└────────┬─────────┘
         │
         ▼
┌──────────────────────┐
│  Notification Prefs  │  /settings/notifications
│  • Mode              │
│  • Quiet hours       │
│  • Categories        │
└────────┬─────────────┘
         │
    ┌────┴────────────────────────────────────────────┐
    │                    │                            │
[Change mode]      [Set quiet hours]         [Toggle category]
    │                    │                            │
    ▼                    ▼                            ▼
┌──────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ Always →     │  │ Set start/end    │  │ Toggle on/off    │
│ App only →   │  │ (time pickers)   │  │ per category     │
│ Off          │  │ e.g. 22:00-08:00 │  │ (Dining, Retail, │
└──────┬───────┘  └──────────────────┘  │  Entertainment)  │
       │                                 └──────────────────┘
       │
  ┌────┴────────────────────┐
  │                         │
[Always → Off]        [Off → Always]
  │                         │
  ▼                         ▼
[Notifications         [Check OS permission]
 stop immediately]          │
                       ┌────┴────┐
                       │Granted? │
                       └────┬────┘
                    No  │   │ Yes
                        │   │
                        ▼   ▼
                  [Show "Enable   [Notifications
                   in device       resume]
                   settings"
                   prompt]
```

---

## Flow 7: Privacy & Data Controls

```
┌──────────────────┐
│  Settings Tab    │  /settings
└────────┬─────────┘
         │
    ┌────┴────────────────────────────────────────────────────┐
    │                    │                    │                │
[Location          [Transaction         [Download         [Delete
 tracking]          data]                my data]          account]
    │                    │                    │                │
    ▼                    ▼                    ▼                ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ Always /     │  │ Allowed /    │  │ Generate     │  │ Confirm      │
│ App only /   │  │ Not allowed  │  │ JSON export  │  │ identity     │
│ Off          │  └──────┬───────┘  └──────┬───────┘  │ (biometric/  │
└──────┬───────┘         │                 │          │  password)   │
       │                 │                 │          └──────┬───────┘
       │            ┌────┴────┐            │                 │
  ┌────┴────┐       │Opt out? │            ▼                 ▼
  │Change?  │       └────┬────┘     ┌──────────────┐  ┌──────────────┐
  └────┬────┘    No  │   │ Yes     │ Processing   │  │ 7-day        │
   No  │ Yes         │   │         │ (async)      │  │ cooling-off  │
   │   │             ▼   ▼         └──────┬───────┘  └──────┬───────┘
   ▼   ▼        [No    ┌──────────┐       │                 │
[No  [OS perm   change]│ Stop sync│       ▼                 │
chg]  dialog           │ Delete   │ ┌──────────────┐   ┌────┴────┐
      if upgrade]      │ spending │ │ Push: "Your  │   │Cancel?  │
                       │ profile  │ │ data is ready│   └────┬────┘
                       │ Degrade  │ │ to download" │  Yes│   │No
                       │ to cats  │ └──────┬───────┘     │   │
                       │ only     │        │             ▼   ▼
                       └──────────┘        ▼        [Cancel  [Delete
                                    ┌──────────────┐ account  all data
                                    │ Download     │ restored] within
                                    │ (24hr link)  │          30 days,
                                    └──────────────┘          email
                                                              confirm]
```

---

## Flow 8: Merchant Campaign Lifecycle (Sprint 3)

```
┌──────────────────┐
│  Merchant        │
│  Registration    │  (Web dashboard)
│  • Business info │
│  • Documents     │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Quality Review  │  (Admin, 48hr SLA)
│  • Verify docs   │
│  • Check business│
└────────┬─────────┘
         │
    ┌────┴────┐
    │Approved?│
    └────┬────┘
 No  │   │ Yes
     │   │
     ▼   ▼
[Rejected ┌──────────────────┐
 + reason]│  Create Campaign │  (Merchant dashboard)
          │  • Offer content │
          │  • Geofence zones│
          │  • Active hours  │
          │  • Target cats   │
          │  • Limits        │
          └────────┬─────────┘
                   │
              ┌────┴────┐
              │Publish? │
              └────┬────┘
          Draft│   │ Publish
               │   │
               ▼   ▼
         [Save  ┌──────────────────┐
          draft] │  Campaign Live  │
                 │  • Geofences    │
                 │    active       │
                 │  • Matching     │
                 │    pipeline     │
                 │    picks up     │
                 └────────┬────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │  Analytics       │  (Merchant dashboard)
                 │  • Impressions   │
                 │  • Redemptions   │
                 │  • Engagement    │
                 └──────────────────┘
```

---

## Flow 9: Offer Reporting & Quality Control

```
┌──────────────────┐
│  Offer Detail    │  /offers/:id
│  [⋯ More menu]   │
└────────┬─────────┘
         │
    [Report issue]
         │
         ▼
┌──────────────────┐
│  Report Type     │  (Bottom sheet)
│  ○ Expired       │
│  ○ Invalid terms │
│  ○ Inappropriate │
│  ○ Other         │
└────────┬─────────┘
         │
    [Submit]
         │
         ▼
┌──────────────────┐
│  Confirmation    │
│  "Thanks! We'll  │
│   review within  │
│   48 hours"      │
└────────┬─────────┘
         │
         ▼ (Backend)
┌──────────────────┐
│  Admin Queue     │
│  • Review report │
│  • 3+ reports =  │
│    auto-pause    │
└────────┬─────────┘
         │
    ┌────┴────────────────┐
    │          │          │
[Dismiss] [Warn      [Deactivate
 report]   merchant]   offer]
    │          │          │
    ▼          ▼          ▼
[Reporter  [Merchant  [Offer removed,
 notified]  notified]  reporter notified]
```

---

## Flow 10: Social Sharing & Viral Loop (Sprint 4)

```
┌──────────────────┐
│  Offer Detail    │
│  [Share button]  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Native Share    │  (OS share sheet)
│  Sheet           │
│  • Messages      │
│  • WhatsApp      │
│  • Copy link     │
│  • More...       │
└────────┬─────────┘
         │
    [Recipient receives deep link]
         │
         ▼
┌──────────────────┐
│  Recipient taps  │
│  link            │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
[Has app]               [No app]
    │                         │
    ▼                         ▼
┌──────────────┐       ┌──────────────┐
│ Open offer   │       │ App Store    │
│ detail       │       │ (offer_id    │
│ directly     │       │  preserved)  │
└──────────────┘       └──────┬───────┘
                              │
                         [Install + Register]
                              │
                              ▼
                       ┌──────────────┐
                       │ Open offer   │
                       │ (deferred    │
                       │  deep link)  │
                       └──────────────┘
```


---

## Flow 11: Unsupported City / Boundary Enforcement

```
┌──────────────────┐
│  User opens app  │
│  (or moves to    │
│   new location)  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Check: Is user  │
│  within a        │
│  supported city? │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
  [Yes]                    [No]
    │                         │
    ▼                         ▼
┌──────────────────┐  ┌──────────────────────────┐
│  Normal offer    │  │  Offers Tab              │
│  feed            │  │  "Coming soon to your    │
│  (geofencing     │  │   area"                  │
│   active)        │  │                          │
└──────────────────┘  │  [illustration]           │
                      │  "We're not in [City]     │
                      │   yet, but we're growing! │
                      │   Get notified when we    │
                      │   launch near you."       │
                      │                          │
                      │  [Notify me] btn-primary  │
                      │  [Browse all] btn-ghost   │
                      └────────────┬─────────────┘
                                   │
                      ┌────────────┴────────────┐
                      │                         │
                 [Notify me]              [Browse all]
                      │                         │
                      ▼                         ▼
               ┌──────────────┐         ┌──────────────┐
               │ Record       │         │ Show all     │
               │ interest     │         │ offers       │
               │ (city + user)│         │ (no distance │
               │ Push when    │         │  sorting)    │
               │ city launches│         └──────────────┘
               └──────────────┘

─── GEOFENCE SUPPRESSION (Backend) ───

┌──────────────────┐
│  Geofence event  │
│  received        │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Is event within │
│  supported city  │
│  boundary?       │
└────────┬─────────┘
         │
    ┌────┴────┐
    │         │
  [Yes]    [No]
    │         │
    ▼         ▼
[Process   [Suppress —
 normally]  no notification,
            no matching,
            log for analytics]
```

---

## Flow 12: Wearable Device Notifications (Apple Watch / Wear OS)

```
┌──────────────────────────────────────────────────────────┐
│  TRIGGER: Offer notification dispatched to user           │
│  (Same trigger as Flow 2 — geofence match)               │
└────────────────────────────────┬─────────────────────────┘
                                 │
                                 ▼
                    ┌──────────────────────┐
                    │  APNs / FCM routes   │
                    │  to paired device    │
                    │  (automatic)         │
                    └────────────┬─────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
              [Phone]                   [Watch]
                    │                         │
                    ▼                         ▼
           ┌──────────────┐       ┌──────────────────────┐
           │ Standard     │       │  Watch Notification  │
           │ push notif   │       │  • Merchant name     │
           │ (Flow 2)     │       │  • Discount value    │
           │              │       │  • Distance          │
           └──────────────┘       └────────────┬─────────┘
                                               │
                                  ┌────────────┴────────────┐
                                  │            │            │
                            [Dismiss]    [View in app] [Complication
                                  │            │        tap]
                                  ▼            ▼            │
                            [Clear]    ┌──────────────┐     │
                                       │ Open phone   │     │
                                       │ app to offer │     │
                                       │ detail       │     │
                                       └──────────────┘     │
                                                            ▼
                                               ┌──────────────────────┐
                                               │  Watch Complication   │
                                               │  (Always visible)     │
                                               │                      │
                                               │  Shows: "3 offers"   │
                                               │  (count of active    │
                                               │   nearby offers)     │
                                               │                      │
                                               │  Tap → opens phone   │
                                               │  app to Offers tab   │
                                               └──────────────────────┘

─── WHY NO REDEMPTION ON WATCH ───

QR codes are too small to scan reliably on watch screens.
Watch always shows: "Open on phone to redeem"
This routes to the phone app's offer detail → QR flow (Flow 3).
```

---

## Flow 13: Network Error & Offline Recovery

```
┌──────────────────┐
│  User action     │
│  (any API call)  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Network         │
│  available?      │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
  [Yes]                    [No]
    │                         │
    ▼                         ▼
┌──────────────────┐  ┌──────────────────────────┐
│  Make request    │  │  OFFLINE MODE            │
└────────┬─────────┘  │                          │
         │            │  Top banner:             │
    ┌────┴────┐       │  "No internet connection"│
    │Success? │       │  (persistent, yellow)    │
    └────┬────┘       │                          │
 No  │   │ Yes       │  Cached content shown:   │
     │   │            │  • Last loaded offers    │
     ▼   ▼            │  • Profile (local)       │
[Error ┌──────────┐   │  • Settings (local)      │
 type?]│ Normal   │   │                          │
     │ │ response │   │  Disabled actions:       │
     │ └──────────┘   │  • Redeem (greyed out)   │
     │                │  • Report                │
     │                │  • Share (link only)     │
     │                │                          │
     │                │  On reconnect:           │
     │                │  • Banner dismisses      │
     │                │  • Auto-refresh feed     │
     │                │  • Sync pending actions  │
     │                └──────────────────────────┘
     │
     ├─── [Timeout (10s)] ──▶ "Taking longer than usual. Check your connection."
     │                        [Retry button]
     │
     ├─── [Server error (5xx)] ──▶ "Something went wrong on our end."
     │                              "Please try again in a few minutes."
     │                              [Retry button]
     │
     ├─── [Auth error (401)] ──▶ [Silent token refresh]
     │                                │
     │                           ┌────┴────┐
     │                           │Success? │
     │                           └────┬────┘
     │                        No  │   │ Yes
     │                            │   │
     │                            ▼   ▼
     │                      [Redirect  [Retry
     │                       to login]  original
     │                                  request]
     │
     └─── [Rate limited (429)] ──▶ "Too many requests. Please wait."
                                    [Auto-retry after Retry-After header]

─── GEOFENCE EVENTS WHILE OFFLINE ───

┌──────────────────┐
│  Device detects  │
│  geofence entry  │
│  (OS-level)      │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Can reach       │
│  server?         │
└────────┬─────────┘
         │
    ┌────┴────┐
    │         │
  [Yes]    [No]
    │         │
    ▼         ▼
[Normal   [Queue event locally.
 pipeline] On reconnect, send
           with original timestamp.
           Server decides if still
           relevant (< 30 min old)]
```

---

## Flow 14: Post-Redemption Review & Rating

```
┌──────────────────┐
│  Offer Redeemed  │  (Flow 3 complete)
│  (24 hours ago)  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Push: "How was  │
│  your experience │
│  at Pizza        │
│  Express?"       │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
  [Tap]                  [Dismiss]
    │                         │
    ▼                         ▼
┌──────────────────┐    [No action —
│  Rating Screen   │     can rate later
│  (Bottom sheet)  │     from History]
│                  │
│  ★ ★ ★ ★ ☆     │
│  (tap to rate)   │
│                  │
│  "Tell us more"  │
│  [optional text  │
│   max 200 chars] │
│                  │
│  [Submit] btn    │
│  [Skip] ghost    │
└────────┬─────────┘
         │
    ┌────┴────────────────────┐
    │                         │
  [Submit]               [Skip]
    │                         │
    ▼                         ▼
┌──────────────────┐    [Dismiss —
│  "Thanks for     │     no rating
│   your feedback!"│     recorded]
│                  │
│  Rating affects: │
│  • Offer ranking │
│    (>4.0 boost,  │
│     <2.5 penalty)│
│  • 3+ reports at │
│    <2.0 → admin  │
│    review        │
└──────────────────┘

─── RATING FROM HISTORY ───

┌──────────────────┐
│  History Tab     │  /history
│  [Redeemed offer │
│   without rating]│
│  "Rate this" link│
└────────┬─────────┘
         │
    [Tap "Rate this"]
         │
         ▼
┌──────────────────┐
│  Rating Screen   │  (Same as above)
└──────────────────┘
```
