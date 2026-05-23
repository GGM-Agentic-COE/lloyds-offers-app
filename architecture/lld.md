# Low-Level Design — Smart Location-Based Offers Platform
**Version:** 1.0 · **Status:** Approved · **Last Updated:** May 2026  
**Stack:** Java 21, Spring Boot 3.x, PostgreSQL 15, Redis 7, Pub/Sub

---

## 1. Domain Model

```
┌─────────────────────────────────────────────────────────────────────────────┐
│ auth-service                                                                 │
│  User ──< OtpCode                                                           │
│  User ──< UserSession                                                       │
│  User ──< Consent                                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│ user-service                                                                 │
│  UserProfile ──1 User (by userId)                                           │
│  NotificationPreference ──1 User (by userId)                                │
├─────────────────────────────────────────────────────────────────────────────┤
│ notification-service                                                         │
│  DeviceToken ──< User (by userId)                                           │
│  NotificationLog ──< User (by userId)                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│ offer-service                                                                │
│  Offer ──< Redemption                                                       │
│  Offer ──< Rating                                                           │
│  Offer ──< Report                                                           │
│  Offer ──< ShareLink                                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│ geofence-service                                                             │
│  GeofenceZone ──< GeofenceEvent                                             │
│  City ──< GeofenceZone                                                      │
│  City ──< CityInterest                                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│ merchant-service                                                             │
│  Merchant ──< Campaign                                                      │
│  Campaign ──< CampaignAnalytics                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Auth-Service — Database Schema

### Table: users

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK, DEFAULT gen_random_uuid() | |
| identifier | VARCHAR(255) | NOT NULL | [ENCRYPTED] Phone or email |
| identifier_type | VARCHAR(10) | NOT NULL, CHECK IN ('phone','email') | |
| password_hash | VARCHAR(255) | NULLABLE | BCrypt, set if user skips biometric |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'pending' | pending/verified/suspended/deleted |
| biometric_enabled | BOOLEAN | NOT NULL, DEFAULT false | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| verified_at | TIMESTAMPTZ | NULLABLE | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| version | INTEGER | NOT NULL, DEFAULT 1 | Optimistic locking |

**Indexes:** `UNIQUE(identifier)`, `idx_users_status`

### Table: otp_codes

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | FK → users(id), NOT NULL | |
| code_hash | VARCHAR(64) | NOT NULL | SHA-256 of 6-digit code |
| expires_at | TIMESTAMPTZ | NOT NULL | created_at + 90s |
| used | BOOLEAN | NOT NULL, DEFAULT false | |
| attempts | INTEGER | NOT NULL, DEFAULT 0 | Max 5 |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_otp_user_id`, `idx_otp_expires_at`  
**Cleanup:** Rows older than 24h purged by scheduled job.

### Table: user_sessions

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | FK → users(id), NOT NULL | |
| device_id | VARCHAR(255) | NOT NULL | |
| device_name | VARCHAR(100) | NULLABLE | |
| refresh_token_hash | VARCHAR(64) | NOT NULL | SHA-256 |
| expires_at | TIMESTAMPTZ | NOT NULL | created_at + 30d |
| last_active_at | TIMESTAMPTZ | NOT NULL | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_sessions_user_device (user_id, device_id)`, `idx_sessions_token_hash`

### Table: consents

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | FK → users(id), NOT NULL | |
| consent_type | VARCHAR(20) | NOT NULL | location/transaction |
| decision | VARCHAR(20) | NOT NULL | always/app_open/off/allow/deny |
| consent_version | VARCHAR(10) | NOT NULL | Policy version |
| recorded_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| superseded_at | TIMESTAMPTZ | NULLABLE | Set when new consent of same type recorded |

**Indexes:** `idx_consents_user_type (user_id, consent_type)`  
**Rule:** Append-only. Never UPDATE or DELETE. Current = WHERE superseded_at IS NULL.

### Table: audit_log

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | NULLABLE | |
| action | VARCHAR(100) | NOT NULL | e.g. 'user.registered' |
| resource_type | VARCHAR(50) | NOT NULL | |
| resource_id | UUID | NULLABLE | |
| metadata | JSONB | NULLABLE | Additional context |
| ip_address | INET | NULLABLE | |
| device_id | VARCHAR(255) | NULLABLE | |
| correlation_id | UUID | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_audit_user_id`, `idx_audit_created_at`  
**Rule:** Append-only. No UPDATE/DELETE. Partitioned by month.

---

## 3. Auth-Service — CQRS Handlers

### RegisterUserCommand

```
Input: { identifier, identifierType, deviceId }
Validation:
  - identifier: E.164 format (phone) or RFC 5322 (email)
  - identifierType: enum [phone, email]
  - deviceId: non-empty string
  - Rate limit: 5 per deviceId per hour (Redis counter)
Logic:
  1. Check if identifier exists in users table → 409 Conflict if found
  2. INSERT into users (status='pending')
  3. Generate 6-digit random code
  4. Store SHA-256(code) in otp_codes with expires_at = now() + 90s
  5. Publish event: user.registered {userId, identifierType}
  6. Dispatch OTP via SMS (phone) or Email (email) — fire-and-forget via Pub/Sub
Output: { userId, verificationRequired: true, expiresIn: 90 }
Events: user.registered
```

### VerifyOtpCommand

```
Input: { userId, code }
Validation:
  - code: exactly 6 digits
  - userId: valid UUID
Logic:
  1. Load latest otp_codes for userId WHERE used=false AND expires_at > now()
  2. If not found → 401 "Code expired"
  3. Increment attempts
  4. If attempts >= 5 → 429 "Locked for 30 minutes"
  5. Compare SHA-256(code) with code_hash
  6. If mismatch → 401 "Invalid code" + attemptsRemaining
  7. Mark otp used=true
  8. Update user status='verified', verified_at=now()
  9. Generate JWT access token (15min) + refresh token (30d)
  10. Store SHA-256(refreshToken) in user_sessions
  11. Publish event: user.verified {userId}
Output: { accessToken, refreshToken, expiresIn: 900, tokenType: "Bearer" }
Events: user.verified
```

### ResendOtpCommand

```
Input: { userId }
Validation:
  - Rate limit: 3 per userId per 10 minutes (Redis counter)
Logic:
  1. Invalidate existing unused OTPs for userId (set used=true)
  2. Generate new 6-digit code
  3. Store SHA-256(code) in otp_codes
  4. Dispatch via SMS/Email
Output: { sent: true, expiresIn: 90 }
```

### LoginCommand

```
Input: { identifier, password, deviceId }
Validation:
  - identifier: non-empty
  - password: non-empty
  - Rate limit: 5 per identifier per 15 minutes
Logic:
  1. Load user by identifier WHERE status='verified'
  2. If not found → 401 "Invalid credentials" (no user enumeration)
  3. Compare BCrypt(password) with password_hash
  4. If mismatch → 401 "Invalid credentials"
  5. Generate JWT access token + refresh token
  6. Store session in user_sessions
Output: { accessToken, refreshToken, expiresIn: 900, tokenType: "Bearer" }
```

### RefreshTokenCommand

```
Input: { refreshToken }
Logic:
  1. Compute SHA-256(refreshToken)
  2. Load session by refresh_token_hash WHERE expires_at > now()
  3. If not found → 401 "Invalid refresh token"
  4. Delete old session (token rotation)
  5. Generate new access + refresh tokens
  6. Store new session
  7. Update last_active_at
Output: { accessToken, refreshToken, expiresIn: 900, tokenType: "Bearer" }
```

### LogoutCommand

```
Input: { deviceId } (from authenticated user context)
Logic:
  1. DELETE from user_sessions WHERE user_id = currentUser AND device_id = deviceId
  2. Audit log: user.logout
Output: { success: true }
```

### RecordConsentCommand

```
Input: { consentType, decision, version } (from authenticated user)
Validation:
  - consentType: enum [location, transaction]
  - decision: enum [always, app_open, off, allow, deny]
Logic:
  1. UPDATE consents SET superseded_at=now() WHERE user_id=current AND consent_type=input AND superseded_at IS NULL
  2. INSERT new consent record
  3. Publish event: consent.recorded {userId, consentType, decision}
  4. Audit log: consent.recorded
Output: { consentId, recordedAt }
Events: consent.recorded
```

### GetConsentQuery

```
Input: (authenticated user context)
Logic:
  1. SELECT * FROM consents WHERE user_id=current AND superseded_at IS NULL
Output: { consents: [{consentType, decision, version, recordedAt}] }
```


---

## 4. User-Service — Database Schema

### Table: user_profiles

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | UNIQUE, NOT NULL | References auth-service user |
| display_name | VARCHAR(50) | NULLABLE | |
| categories | JSONB | NOT NULL, DEFAULT '[]' | Array of category strings |
| budget_sensitivity | VARCHAR(10) | NOT NULL, DEFAULT 'medium' | low/medium/high |
| preferred_radius_meters | INTEGER | NOT NULL, DEFAULT 2000 | 500-5000 |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| version | INTEGER | NOT NULL, DEFAULT 1 | |

**Indexes:** `UNIQUE(user_id)`

### Table: notification_preferences

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | UNIQUE, NOT NULL | |
| mode | VARCHAR(10) | NOT NULL, DEFAULT 'always' | always/app_open/off |
| quiet_hours_start | TIME | NULLABLE | e.g. 22:00 |
| quiet_hours_end | TIME | NULLABLE | e.g. 08:00 |
| categories | JSONB | NOT NULL, DEFAULT '{}' | {dining: true, retail: true, ...} |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `UNIQUE(user_id)`

---

## 5. User-Service — CQRS Handlers

### GetUserQuery

```
Input: (authenticated user context → userId)
Logic:
  1. Call auth-service internal API or read from local cache: user basic info
  2. Return id, identifier (masked), status, biometricEnabled
Output: { id, identifier, identifierType, status, displayName, biometricEnabled }
```

### UpdateBiometricCommand

```
Input: { enabled } (from authenticated user)
Logic:
  1. Call auth-service internal: PATCH user biometric_enabled
Output: { updated: true }
```

### GetProfileQuery

```
Input: (userId from token)
Logic:
  1. SELECT * FROM user_profiles WHERE user_id = current
  2. If not found → create default profile (lazy initialization)
Output: { categories, budgetSensitivity, preferredRadiusMeters }
```

### UpdateProfileCommand

```
Input: { categories?, budgetSensitivity?, preferredRadiusMeters? }
Validation:
  - categories: array of valid category strings
  - budgetSensitivity: enum [low, medium, high]
  - preferredRadiusMeters: integer 500-5000
Logic:
  1. UPSERT user_profiles with provided fields
  2. updated_at = now(), version++
Output: { updated: true }
```

### GetNotificationPrefsQuery

```
Input: (userId)
Logic:
  1. SELECT * FROM notification_preferences WHERE user_id = current
  2. If not found → return defaults (mode=always, all categories enabled)
Output: { mode, quietHours: {start, end}, categories }
```

### UpdateNotificationPrefsCommand

```
Input: { mode?, quietHours?: {start, end}, categories? }
Validation:
  - mode: enum [always, app_open, off]
  - quietHours.start/end: valid time format HH:mm
Logic:
  1. UPSERT notification_preferences
  2. updated_at = now()
Output: { updated: true }
```

### DeleteAccountCommand

```
Input: { password } (from authenticated user)
Logic:
  1. Verify password against auth-service
  2. Set user status = 'pending_deletion'
  3. Schedule deletion job for now() + 7 days
  4. Publish event: user.deletion_scheduled {userId, scheduledAt}
  5. Audit log: account.deletion_requested
Output: { scheduledAt: now()+7d, cancelBefore: now()+7d }
```

### RequestExportCommand

```
Input: (userId)
Validation:
  - Rate limit: 1 per userId per 7 days
Logic:
  1. Create export job record (status=processing)
  2. Publish event: user.export_requested {userId, jobId}
  3. Async worker: gather data from all services → generate JSON → upload to GCS → set signed URL (24h)
  4. Notify user via push when ready
Output: { jobId, status: "processing" }
```

---

## 6. Notification-Service — Database Schema

### Table: device_tokens

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | NOT NULL | |
| device_id | VARCHAR(255) | NOT NULL | |
| platform | VARCHAR(10) | NOT NULL | ios/android |
| push_token | VARCHAR(512) | NOT NULL | [ENCRYPTED] APNs/FCM token |
| is_active | BOOLEAN | NOT NULL, DEFAULT true | |
| registered_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| last_used_at | TIMESTAMPTZ | NULLABLE | |

**Indexes:** `UNIQUE(user_id, device_id)`, `idx_device_tokens_active (user_id, is_active)`

### Table: notification_log

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | NOT NULL | |
| offer_id | UUID | NULLABLE | |
| notification_type | VARCHAR(50) | NOT NULL | offer/system/expiry |
| title | VARCHAR(100) | NOT NULL | |
| body | VARCHAR(300) | NOT NULL | |
| deep_link | VARCHAR(500) | NULLABLE | |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'queued' | queued/sent/delivered/failed/read |
| sent_at | TIMESTAMPTZ | NULLABLE | |
| delivered_at | TIMESTAMPTZ | NULLABLE | |
| read_at | TIMESTAMPTZ | NULLABLE | |
| failure_reason | VARCHAR(200) | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_notif_user_status (user_id, status)`, `idx_notif_created_at`  
**Retention:** 90 days, then archived.

---

## 7. Notification-Service — CQRS Handlers

### RegisterDeviceCommand

```
Input: { deviceId, platform, pushToken } (from authenticated user)
Validation:
  - platform: enum [ios, android]
  - pushToken: non-empty, max 512 chars
Logic:
  1. UPSERT device_tokens ON (user_id, device_id)
  2. Set is_active=true, registered_at=now()
Output: { registered: true }
```

### DeregisterDeviceCommand

```
Input: { deviceId } (from authenticated user)
Logic:
  1. UPDATE device_tokens SET is_active=false WHERE user_id=current AND device_id=input
Output: { deleted: true }
```

### SendNotificationCommand (Internal)

```
Input: { userId, title, body, deepLink?, imageUrl?, offerId? }
Auth: X-Service-Auth header (service-to-service)
Logic:
  1. Load user's notification preferences (call user-service or cache)
  2. If mode='off' → skip, return {status: 'suppressed'}
  3. Check quiet hours → if in quiet hours, queue for later
  4. Check frequency cap (Redis): GET cap:{userId}:{offerId}:{date}
     - If exists → skip, return {status: 'capped'}
  5. Load active device_tokens for userId
  6. For each device:
     - iOS: dispatch via APNs (HTTP/2)
     - Android: dispatch via FCM (REST)
  7. INSERT notification_log (status='sent')
  8. SET Redis key cap:{userId}:{offerId}:{date} TTL=86400
  9. Increment daily counter cap:{userId}:daily:{date}
  10. Publish event: notification.delivered or notification.failed
Output: { notificationId, status: 'sent'|'suppressed'|'capped' }
Events: notification.delivered | notification.failed
```

### CheckFrequencyCapQuery (Internal)

```
Input: { userId, offerId }
Logic:
  1. Check Redis: EXISTS cap:{userId}:{offerId}:{today}
  2. Check Redis: GET cap:{userId}:daily:{today} >= 5
  3. Check Redis: GET cap:{userId}:last_sent → if < 30min ago, ineligible
Output: { eligible: boolean, reason?: 'offer_cap'|'daily_cap'|'cooldown' }
```

### GetNotificationHistoryQuery

```
Input: { pageNumber, pageSize } (from authenticated user)
Logic:
  1. SELECT FROM notification_log WHERE user_id=current ORDER BY created_at DESC
  2. LIMIT pageSize OFFSET (pageNumber-1)*pageSize
Output: { data: [...], meta: { page, pageSize, totalCount } }
```

### MarkNotificationReadCommand

```
Input: { notificationId } (from authenticated user)
Logic:
  1. UPDATE notification_log SET status='read', read_at=now() WHERE id=input AND user_id=current
Output: { updated: true }
```


---

## 8. Offer-Service — Database Schema

### Table: offers

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| campaign_id | UUID | NOT NULL | References merchant-service |
| merchant_id | UUID | NOT NULL | Denormalized for query perf |
| title | VARCHAR(60) | NOT NULL | |
| description | VARCHAR(200) | NOT NULL | |
| terms | VARCHAR(500) | NULLABLE | |
| image_url | VARCHAR(500) | NULLABLE | CDN URL |
| discount_type | VARCHAR(15) | NOT NULL | percentage/fixed/bogof |
| discount_value | DECIMAL(10,2) | NOT NULL | |
| zone_id | UUID | NOT NULL | Geofence zone |
| merchant_name | VARCHAR(100) | NOT NULL | Denormalized |
| merchant_address | VARCHAR(200) | NULLABLE | |
| center_lat | DECIMAL(9,6) | NOT NULL | Zone center |
| center_lng | DECIMAL(9,6) | NOT NULL | |
| max_redemptions | INTEGER | NULLABLE | NULL = unlimited |
| max_per_user | INTEGER | NOT NULL, DEFAULT 1 | |
| redemption_count | INTEGER | NOT NULL, DEFAULT 0 | |
| status | VARCHAR(15) | NOT NULL, DEFAULT 'active' | active/paused/expired |
| valid_from | TIMESTAMPTZ | NOT NULL | |
| valid_to | TIMESTAMPTZ | NOT NULL | |
| active_hours_start | TIME | NULLABLE | |
| active_hours_end | TIME | NULLABLE | |
| target_categories | JSONB | NOT NULL, DEFAULT '[]' | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_offers_status_valid (status, valid_to)`, `idx_offers_zone`, `idx_offers_merchant`  
**Spatial:** `idx_offers_location` on (center_lat, center_lng) for proximity queries.

### Table: redemptions

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| offer_id | UUID | FK → offers(id), NOT NULL | |
| user_id | UUID | NOT NULL | |
| redemption_token | UUID | UNIQUE, NOT NULL | QR code token |
| manual_code | VARCHAR(8) | NOT NULL | Alphanumeric fallback |
| status | VARCHAR(15) | NOT NULL, DEFAULT 'pending' | pending/confirmed/expired |
| token_expires_at | TIMESTAMPTZ | NOT NULL | created_at + 5min |
| confirmed_at | TIMESTAMPTZ | NULLABLE | |
| confirmed_by | VARCHAR(50) | NULLABLE | merchant device ID |
| savings_amount | DECIMAL(10,2) | NULLABLE | Calculated on confirm |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `UNIQUE(offer_id, user_id)` (prevents duplicate), `idx_redemptions_token`, `idx_redemptions_user_status`

### Table: ratings

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| offer_id | UUID | FK → offers(id), NOT NULL | |
| user_id | UUID | NOT NULL | |
| stars | INTEGER | NOT NULL, CHECK 1-5 | |
| review_text | VARCHAR(200) | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `UNIQUE(offer_id, user_id)`, `idx_ratings_offer`

### Table: reports

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| offer_id | UUID | FK → offers(id), NOT NULL | |
| user_id | UUID | NOT NULL | |
| report_type | VARCHAR(20) | NOT NULL | expired/invalid/inappropriate/other |
| description | VARCHAR(500) | NULLABLE | |
| status | VARCHAR(15) | NOT NULL, DEFAULT 'pending' | pending/reviewed/dismissed |
| reviewed_at | TIMESTAMPTZ | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_reports_offer_status`, `idx_reports_pending`

### Table: share_links

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| offer_id | UUID | FK → offers(id), NOT NULL | |
| user_id | UUID | NOT NULL | Sharer (anonymous to recipient) |
| short_code | VARCHAR(10) | UNIQUE, NOT NULL | URL slug |
| clicks | INTEGER | NOT NULL, DEFAULT 0 | |
| installs | INTEGER | NOT NULL, DEFAULT 0 | Attribution |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `UNIQUE(short_code)`

---

## 9. Offer-Service — CQRS Handlers

### GetNearbyOffersQuery

```
Input: { lat, lng, radiusMeters?, categories?, sort?, pageNumber, pageSize }
Defaults: radiusMeters=2000, sort='nearest', pageNumber=1, pageSize=20
Logic:
  1. Calculate bounding box from lat/lng/radius
  2. SELECT offers WHERE status='active' AND valid_to > now()
     AND center_lat BETWEEN box AND center_lng BETWEEN box
     AND (active_hours check: current time within start/end OR null)
  3. Filter by categories if provided (JSONB overlap)
  4. Calculate distance for each: haversine(lat, lng, center_lat, center_lng)
  5. Filter: distance <= radiusMeters
  6. Sort by: nearest (distance ASC) | newest (created_at DESC) | expiring (valid_to ASC)
  7. Paginate
Output: { data: [{id, title, merchantName, discountType, discountValue, distanceMeters, expiresAt, category, imageUrl}], meta: {page, pageSize, total} }
```

### GetOfferDetailQuery

```
Input: { offerId, userLat?, userLng? }
Logic:
  1. SELECT offer by id
  2. Calculate distance if user coords provided
  3. Load average rating: AVG(stars) FROM ratings WHERE offer_id
  4. Check if current user has already redeemed
Output: { id, title, description, terms, imageUrl, discountType, discountValue, merchantName, merchantAddress, distanceMeters, expiresAt, averageRating, ratingCount, alreadyRedeemed }
```

### RedeemOfferCommand

```
Input: { offerId } (from authenticated user)
Validation:
  - Offer exists and status='active' and valid_to > now()
  - User hasn't already redeemed (UNIQUE constraint)
  - redemption_count < max_redemptions (if set)
Logic:
  1. Generate redemptionToken (UUID) and manualCode (8-char alphanumeric)
  2. INSERT redemption (status='pending', token_expires_at=now()+5min)
  3. Increment offers.redemption_count
Output: { redemptionToken, manualCode, expiresAt }
```

### ConfirmRedemptionCommand (Merchant)

```
Input: { redemptionToken } (from merchant auth)
Validation:
  - Token exists, status='pending', token_expires_at > now()
  - Merchant ID matches offer's merchant_id
Logic:
  1. Load redemption by token
  2. Update status='confirmed', confirmed_at=now()
  3. Calculate savings_amount based on discount
  4. Publish event: offer.redeemed {offerId, userId, merchantId, savingsAmount}
Output: { confirmed: true, offerTitle, savingsAmount }
Events: offer.redeemed
```

### GetOfferHistoryQuery

```
Input: { status?, pageNumber, pageSize } (from authenticated user)
Logic:
  1. SELECT r.*, o.title, o.merchant_name, o.discount_value
     FROM redemptions r JOIN offers o ON r.offer_id = o.id
     WHERE r.user_id = current
     AND (status filter: 'redeemed' → r.status='confirmed', 'expired' → o.valid_to < now())
  2. Calculate totalSaved: SUM(savings_amount) WHERE status='confirmed'
  3. Paginate
Output: { data: [...], meta: {page, total}, totalSaved }
```

### ReportOfferCommand

```
Input: { offerId, reportType, description? }
Validation:
  - reportType: enum [expired, invalid, inappropriate, other]
  - Rate limit: 10 per user per day
Logic:
  1. INSERT report
  2. Check: COUNT reports WHERE offer_id AND status='pending' >= 3 → auto-pause offer
  3. If auto-paused: UPDATE offers SET status='paused', publish campaign.paused event
Output: { reportId, status: 'submitted' }
```

### RateOfferCommand

```
Input: { offerId, stars, reviewText? }
Validation:
  - stars: 1-5
  - reviewText: max 200 chars
  - User must have redeemed this offer
Logic:
  1. Check redemption exists for (user, offer) with status='confirmed'
  2. UPSERT rating (user can update their rating)
  3. If avg rating < 2.0 AND count >= 10 → flag for admin review
Output: { rated: true }
```

### ShareOfferCommand

```
Input: { offerId } (from authenticated user)
Logic:
  1. Generate short_code (6-char base62)
  2. INSERT share_link
  3. Construct URL: https://offers.lloyds.com/s/{short_code}
Output: { shareUrl, offerId }
```

---

## 10. Geofence-Service — Database Schema

### Table: geofence_zones

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| merchant_id | UUID | NOT NULL | |
| city_id | UUID | FK → cities(id), NULLABLE | |
| center_lat | DECIMAL(9,6) | NOT NULL | |
| center_lng | DECIMAL(9,6) | NOT NULL | |
| radius_meters | INTEGER | NOT NULL, CHECK 50-5000 | |
| geom | GEOMETRY(Point, 4326) | NOT NULL | PostGIS point |
| active_hours_start | TIME | NULLABLE | |
| active_hours_end | TIME | NULLABLE | |
| days_of_week | JSONB | NOT NULL, DEFAULT '["MON","TUE","WED","THU","FRI","SAT","SUN"]' | |
| status | VARCHAR(10) | NOT NULL, DEFAULT 'active' | active/paused |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `GIST(geom)` (spatial), `idx_zones_merchant`, `idx_zones_status`

### Table: cities

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| name | VARCHAR(100) | NOT NULL | |
| country_code | VARCHAR(2) | NOT NULL, DEFAULT 'GB' | |
| boundary | GEOMETRY(Polygon, 4326) | NOT NULL | PostGIS polygon |
| status | VARCHAR(10) | NOT NULL, DEFAULT 'active' | active/coming_soon |
| launched_at | TIMESTAMPTZ | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `GIST(boundary)` (spatial)

### Table: geofence_events

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| user_id | UUID | NOT NULL | Hashed in logs |
| zone_id | UUID | FK → geofence_zones(id), NOT NULL | |
| event_type | VARCHAR(5) | NOT NULL | entry/exit |
| accuracy_meters | DECIMAL(6,1) | NOT NULL | |
| timestamp | TIMESTAMPTZ | NOT NULL | Device timestamp |
| processed | BOOLEAN | NOT NULL, DEFAULT false | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_events_user_zone_date (user_id, zone_id, created_at)`, `idx_events_unprocessed`  
**Retention:** 24 hours, then purged (data minimization).

### Table: city_interests

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| city_id | UUID | FK → cities(id), NOT NULL | |
| user_id | UUID | NULLABLE | Authenticated users |
| email | VARCHAR(255) | NULLABLE | Anonymous users |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

---

## 11. Geofence-Service — CQRS Handlers

### CreateZoneCommand (Merchant)

```
Input: { merchantId, centerLat, centerLng, radiusMeters, activeHoursStart?, activeHoursEnd?, daysOfWeek? }
Validation:
  - radiusMeters: 50-5000
  - Coordinates: valid lat (-90 to 90), lng (-180 to 180)
  - Point must fall within a supported city boundary
Logic:
  1. Validate point within city: ST_Contains(city.boundary, ST_Point(lng, lat))
  2. INSERT zone with geom = ST_SetSRID(ST_Point(lng, lat), 4326)
Output: { zoneId, status: 'active' }
```

### ProcessGeofenceEventCommand

```
Input: { userId, zoneId, eventType, timestamp, accuracyMeters }
Validation:
  - eventType: enum [entry, exit]
  - timestamp: not older than 30 minutes
  - accuracyMeters: < 100 (reject inaccurate readings)
Logic:
  1. INSERT geofence_event
  2. If eventType='exit' → no further action
  3. If eventType='entry':
     a. Load zone → check status='active', current time within active_hours, current day in days_of_week
     b. Check city boundary: is zone's city active?
     c. If all pass → publish event: geofence.entry {userId, zoneId, merchantId, timestamp}
  4. Return 202 Accepted (async processing downstream)
Output: { accepted: true }
Events: geofence.entry (if entry + all checks pass)
```

### GetNearbyZonesQuery

```
Input: { lat, lng, radiusMeters }
Logic:
  1. SELECT zones WHERE ST_DWithin(geom, ST_Point(lng, lat)::geography, radiusMeters) AND status='active'
Output: { zones: [{id, merchantId, centerLat, centerLng, radiusMeters}] }
```

### UpdateZoneCommand (Merchant)

```
Input: { zoneId, radiusMeters?, activeHoursStart?, activeHoursEnd?, daysOfWeek?, status? }
Validation:
  - Zone belongs to authenticated merchant
  - radiusMeters: 50-5000 if provided
Logic:
  1. UPDATE geofence_zones SET provided fields, updated_at=now()
Output: { updated: true }
```

### DeleteZoneCommand (Merchant)

```
Input: { zoneId }
Validation:
  - Zone belongs to authenticated merchant
  - No active campaigns reference this zone (or warn)
Logic:
  1. UPDATE geofence_zones SET status='deleted' (soft delete)
Output: { deleted: true }
```

### GetSupportedCitiesQuery

```
Input: none (public)
Logic:
  1. SELECT id, name, status, boundary FROM cities ORDER BY name
Output: { cities: [{id, name, status, boundary (GeoJSON)}] }
```

### RegisterCityInterestCommand

```
Input: { cityId, email? } (userId from token if authenticated)
Logic:
  1. UPSERT city_interest
Output: { registered: true }
```


---

## 12. Merchant-Service — Database Schema

### Table: merchants

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| business_name | VARCHAR(200) | NOT NULL | |
| registration_number | VARCHAR(20) | NOT NULL | |
| business_type | VARCHAR(30) | NOT NULL | restaurant/retail/health_beauty/entertainment/services |
| address | VARCHAR(300) | NOT NULL | |
| contact_email | VARCHAR(255) | NOT NULL | |
| contact_phone | VARCHAR(20) | NULLABLE | |
| document_urls | JSONB | NOT NULL, DEFAULT '[]' | GCS signed URLs |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'submitted' | submitted/under_review/approved/rejected |
| rejection_reason | VARCHAR(500) | NULLABLE | |
| password_hash | VARCHAR(255) | NOT NULL | BCrypt |
| approved_at | TIMESTAMPTZ | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `UNIQUE(registration_number)`, `UNIQUE(contact_email)`, `idx_merchants_status`

### Table: campaigns

| Column | Type | Constraints | Notes |
|---|---|---|---|
| id | UUID | PK | |
| merchant_id | UUID | FK → merchants(id), NOT NULL | |
| title | VARCHAR(60) | NOT NULL | |
| description | VARCHAR(200) | NOT NULL | |
| terms | VARCHAR(500) | NULLABLE | |
| image_url | VARCHAR(500) | NULLABLE | |
| discount_type | VARCHAR(15) | NOT NULL | percentage/fixed/bogof |
| discount_value | DECIMAL(10,2) | NOT NULL | |
| zone_ids | JSONB | NOT NULL | Array of geofence zone UUIDs |
| active_hours_start | TIME | NULLABLE | |
| active_hours_end | TIME | NULLABLE | |
| target_categories | JSONB | NOT NULL, DEFAULT '[]' | |
| max_redemptions | INTEGER | NULLABLE | |
| max_per_user | INTEGER | NOT NULL, DEFAULT 1 | |
| valid_from | TIMESTAMPTZ | NOT NULL | |
| valid_to | TIMESTAMPTZ | NOT NULL | |
| status | VARCHAR(15) | NOT NULL, DEFAULT 'draft' | draft/active/paused/expired |
| published_at | TIMESTAMPTZ | NULLABLE | |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |
| updated_at | TIMESTAMPTZ | NOT NULL, DEFAULT now() | |

**Indexes:** `idx_campaigns_merchant_status (merchant_id, status)`, `idx_campaigns_active (status, valid_to)`

### Table: analytics_events (BigQuery)

| Column | Type | Notes |
|---|---|---|
| event_id | STRING | UUID |
| campaign_id | STRING | |
| merchant_id | STRING | |
| event_type | STRING | impression/redemption/click/share |
| user_id_hash | STRING | SHA-256 hashed (no PII) |
| timestamp | TIMESTAMP | |
| metadata | JSON | Additional context |

**Partitioned by:** timestamp (daily)  
**Clustered by:** campaign_id, event_type

---

## 13. Merchant-Service — CQRS Handlers

### RegisterMerchantCommand

```
Input: { businessName, registrationNumber, businessType, address, contactEmail, password, documents[] }
Validation:
  - registrationNumber: unique, valid format
  - contactEmail: RFC 5322, unique
  - documents: 1-5 files, each max 10MB, PDF/JPG/PNG
  - password: min 12 chars, complexity rules
Logic:
  1. Upload documents to GCS → get signed URLs
  2. INSERT merchant (status='submitted')
  3. Hash password with BCrypt (work factor 12)
  4. Notify admin queue (Pub/Sub: merchant.submitted)
Output: { merchantId, status: 'submitted' }
```

### GetMerchantProfileQuery

```
Input: (from merchant auth token)
Logic:
  1. SELECT * FROM merchants WHERE id = currentMerchant
Output: { id, businessName, businessType, status, approvedAt, ... }
```

### CreateCampaignCommand

```
Input: { title, description, terms?, imageUrl?, discountType, discountValue, zoneIds, activeHoursStart?, activeHoursEnd?, targetCategories, maxRedemptions?, maxPerUser, validFrom, validTo }
Validation:
  - Merchant status must be 'approved'
  - zoneIds: all must belong to this merchant (verify via geofence-service)
  - validFrom < validTo, validFrom >= now()
  - discountValue > 0
Logic:
  1. Validate zone ownership (call geofence-service)
  2. INSERT campaign (status='draft')
Output: { campaignId, status: 'draft' }
```

### GetCampaignsQuery

```
Input: { status?, pageNumber, pageSize } (from merchant auth)
Logic:
  1. SELECT FROM campaigns WHERE merchant_id = current
  2. Filter by status if provided
  3. ORDER BY created_at DESC, paginate
Output: { data: [{id, title, status, discountType, discountValue, validFrom, validTo, redemptionCount}], meta: {page, total} }
```

### UpdateCampaignCommand

```
Input: { campaignId, title?, description?, terms?, imageUrl?, discountValue?, activeHoursStart?, activeHoursEnd?, targetCategories?, maxRedemptions?, validTo? }
Validation:
  - Campaign belongs to merchant
  - Campaign status must be 'draft' or 'paused' (cannot edit active)
  - validTo > now() if provided
Logic:
  1. UPDATE campaigns SET provided fields, updated_at=now()
Output: { updated: true }
```

### PublishCampaignCommand

```
Input: { campaignId } (from merchant auth)
Validation:
  - Campaign belongs to merchant
  - Campaign status = 'draft' or 'paused'
  - validTo > now()
Logic:
  1. UPDATE campaign SET status='active', published_at=now()
  2. For each zoneId: create offer in offer-service (via Pub/Sub event)
  3. Publish event: campaign.published {campaignId, merchantId, zoneIds}
Output: { status: 'active', publishedAt }
Events: campaign.published
```

### PauseCampaignCommand

```
Input: { campaignId } (from merchant auth)
Validation:
  - Campaign status = 'active'
Logic:
  1. UPDATE campaign SET status='paused'
  2. Publish event: campaign.paused {campaignId}
  3. Downstream: offer-service pauses related offers
Output: { status: 'paused' }
Events: campaign.paused
```

### GetAnalyticsOverviewQuery

```
Input: { startDate, endDate } (from merchant auth)
Logic:
  1. Query BigQuery:
     SELECT event_type, COUNT(*) as count
     FROM analytics_events
     WHERE merchant_id = current AND timestamp BETWEEN start AND end
     GROUP BY event_type
  2. Calculate conversion: redemptions / impressions
Output: { impressions, redemptions, conversionRate, uniqueUsers, dateRange }
```

### GetCampaignAnalyticsQuery

```
Input: { campaignId, startDate, endDate }
Logic:
  1. Query BigQuery:
     SELECT DATE(timestamp) as date, event_type, COUNT(*) as count
     FROM analytics_events
     WHERE campaign_id = input AND timestamp BETWEEN start AND end
     GROUP BY date, event_type
     ORDER BY date
Output: { campaignId, dailyBreakdown: [{date, impressions, redemptions, clicks}], totals }
```

### ExportAnalyticsCommand

```
Input: { startDate, endDate, format: 'csv' } (from merchant auth)
Logic:
  1. Create async export job
  2. Query BigQuery → write to GCS as CSV
  3. Generate signed download URL (24h expiry)
  4. Notify merchant via email when ready
Output: { jobId, status: 'processing' }
```

---

## 14. State Machines

### User Account State Machine

| From | To | Trigger | Validations | Side Effects |
|---|---|---|---|---|
| — | pending | RegisterUserCommand | Valid identifier, not duplicate | Send OTP, publish user.registered |
| pending | verified | VerifyOtpCommand | Valid OTP, not expired, attempts < 5 | Issue tokens, publish user.verified |
| verified | suspended | Admin action | Admin authorization | Revoke all sessions |
| verified | pending_deletion | DeleteAccountCommand | Password confirmed | Schedule 7-day deletion |
| pending_deletion | verified | User logs back in | Within 7-day window | Cancel deletion job |
| pending_deletion | deleted | Scheduled job | 7 days elapsed | Purge all data, publish user.deleted |
| pending | expired | Scheduled job | 24h without verification | Purge record |

### Offer State Machine

| From | To | Trigger | Validations | Side Effects |
|---|---|---|---|---|
| — | active | campaign.published event | Campaign valid, zones active | Available for matching |
| active | paused | campaign.paused event OR 3+ reports | — | Stop matching, stop notifications |
| paused | active | campaign.published event (re-publish) | Reports resolved | Resume matching |
| active | expired | Scheduled job (valid_to < now()) | — | Remove from feed, notify "expiring soon" users |
| expired | — | Terminal | — | Retained for history |

### Campaign State Machine

| From | To | Trigger | Validations | Side Effects |
|---|---|---|---|---|
| — | draft | CreateCampaignCommand | Merchant approved | — |
| draft | active | PublishCampaignCommand | valid_to > now(), zones valid | Create offers, publish event |
| active | paused | PauseCampaignCommand | — | Pause offers, publish event |
| paused | active | PublishCampaignCommand | valid_to > now() | Resume offers |
| active | expired | Scheduled job | valid_to < now() | Expire offers |
| draft | deleted | Merchant deletes | — | Soft delete |

### Merchant Onboarding State Machine

| From | To | Trigger | Validations | Side Effects |
|---|---|---|---|---|
| — | submitted | RegisterMerchantCommand | Valid docs, unique reg number | Notify admin queue |
| submitted | under_review | Admin picks up | — | — |
| under_review | approved | Admin approves | Docs verified | Email merchant, enable campaigns |
| under_review | rejected | Admin rejects | Reason provided | Email merchant with reason |
| rejected | submitted | Merchant resubmits | Updated docs | Re-enter queue |

### Redemption State Machine

| From | To | Trigger | Validations | Side Effects |
|---|---|---|---|---|
| — | pending | RedeemOfferCommand | Offer active, not already redeemed, within limits | Generate QR + code |
| pending | confirmed | ConfirmRedemptionCommand | Token valid, not expired, merchant matches | Publish offer.redeemed, increment count |
| pending | expired | Scheduled job | token_expires_at < now() | Free up redemption slot |

---

## 15. Scheduled Jobs

| Job | Schedule | Service | Logic |
|---|---|---|---|
| Offer expiry | Every 15 min | offer-service | Expire offers where valid_to < now() AND status='active' |
| OTP cleanup | Every 1 hour | auth-service | DELETE otp_codes WHERE created_at < now() - 24h |
| Geofence event purge | Every 1 hour | geofence-service | DELETE geofence_events WHERE created_at < now() - 24h |
| Redemption token expiry | Every 1 min | offer-service | UPDATE redemptions SET status='expired' WHERE token_expires_at < now() AND status='pending' |
| Account deletion | Every 1 hour | user-service | Process pending_deletion users where scheduled_at < now() |
| Analytics aggregation | Daily 02:00 | merchant-service | Aggregate raw events into daily summaries in BigQuery |
| Notification log cleanup | Weekly | notification-service | DELETE notification_log WHERE created_at < now() - 90d |
