# EP-01 Manual Test Cases — User Authentication & Registration
**Epic:** EP-01 · **Stories:** US-006 to US-013 · **Type:** Integration (UI + API)  
**Regression Pack:** Yes — run on every sprint delivery

---

## TC-001: Successful Registration with Phone Number

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, EP-01 |
| Linked Story | US-006 |
| Preconditions | App installed, no existing account for test phone |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Launch app | — | Registration screen displayed with phone/email input |
| 2 | Enter phone number | +447700900001 | Input accepted, no error shown |
| 3 | Tap 'Continue' button | — | Loading spinner shown, then OTP screen displayed |
| 4 | Verify API call | POST /api/v1/auth/register | 201 response with userId and verificationRequired: true |

**Postconditions:** User record created with status 'pending'. OTP sent to phone.

---

## TC-002: Successful Registration with Email

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, EP-01 |
| Linked Story | US-006 |
| Preconditions | App installed, no existing account for test email |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Launch app | — | Registration screen displayed |
| 2 | Enter email address | testuser@example.com | Input accepted |
| 3 | Tap 'Continue' | — | OTP screen displayed, email sent |
| 4 | Verify API call | POST /api/v1/auth/register | 201 with userId |

---

## TC-003: Registration with Invalid Phone Format

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, negative, EP-01 |
| Linked Story | US-006, US-013 |
| Preconditions | App on registration screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Enter invalid phone | 12345 | Inline error: "Please enter a valid phone number or email address" |
| 2 | Verify 'Continue' button | — | Button remains disabled |
| 3 | Enter another invalid | abc@@ | Same error displayed |
| 4 | Enter valid phone | +447700900002 | Error clears, button becomes active |

---

## TC-004: Registration with Duplicate Account

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, negative, EP-01 |
| Linked Story | US-006, US-013 |
| Preconditions | Account already exists for +447700900099 |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Enter existing phone | +447700900099 | — |
| 2 | Tap 'Continue' | — | Error: "An account with this already exists — sign in instead" with link |
| 3 | Verify API response | — | 409 Conflict |
| 4 | Tap 'sign in' link | — | Navigates to login screen |

---

## TC-005: Registration Rate Limiting

| Attribute | Value |
|---|---|
| Priority | Medium |
| Component | Auth |
| Labels | regression, security, EP-01 |
| Linked Story | US-006 |
| Preconditions | Same device, fresh rate limit window |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1-5 | Register 5 different numbers | +44770090000X (X=1-5) | All succeed with 201 |
| 6 | Attempt 6th registration | +447700900006 | 429 response: "Too many attempts — please try again later" |
| 7 | Verify Retry-After header | — | Header present with seconds until reset |

---

## TC-006: Successful OTP Verification

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, EP-01 |
| Linked Story | US-007 |
| Preconditions | User registered (status=pending), OTP sent |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Observe OTP screen | — | 6 input boxes displayed, timer counting down from 1:30 |
| 2 | Enter correct OTP | 123456 (from SMS/email) | — |
| 3 | Tap 'Verify' | — | Success, navigates to biometric screen |
| 4 | Verify API response | POST /api/v1/auth/verify-otp | 200 with accessToken, refreshToken |
| 5 | Verify user status | — | User status = 'verified' |

---

## TC-007: OTP Verification with Wrong Code

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, negative, EP-01 |
| Linked Story | US-007 |
| Preconditions | User registered, OTP sent |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Enter wrong OTP | 000000 | — |
| 2 | Tap 'Verify' | — | Error: "Invalid code — 4 attempts remaining" |
| 3 | Enter wrong again | 111111 | Error: "Invalid code — 3 attempts remaining" |
| 4 | Verify API response | — | 401 with attemptsRemaining: 3 |

---

## TC-008: OTP Expiry (90 seconds)

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, boundary, EP-01 |
| Linked Story | US-007 |
| Preconditions | User registered, OTP sent |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Wait 90 seconds | — | Timer reaches 0:00 |
| 2 | Enter the original OTP | 123456 | — |
| 3 | Tap 'Verify' | — | Error: "Code expired" with 'Resend' button visible |
| 4 | Verify API response | — | 401 "Code expired" |

---

## TC-009: OTP Resend Rate Limiting

| Attribute | Value |
|---|---|
| Priority | Medium |
| Component | Auth |
| Labels | regression, security, EP-01 |
| Linked Story | US-007 |
| Preconditions | User on OTP screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Tap 'Resend code' | — | New OTP sent, timer resets |
| 2 | Tap 'Resend code' again | — | New OTP sent |
| 3 | Tap 'Resend code' 3rd time | — | New OTP sent |
| 4 | Tap 'Resend code' 4th time | — | Error: "Too many requests — try again in 10 minutes" |
| 5 | Verify API response | — | 429 with Retry-After header |

---

## TC-010: OTP Lockout After 5 Failed Attempts

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, security, EP-01 |
| Linked Story | US-007 |
| Preconditions | User on OTP screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1-5 | Enter wrong OTP 5 times | 000001-000005 | Attempts remaining decreases each time |
| 6 | Attempt 6th entry | 000006 | Account locked: "Too many attempts. Try again in 30 minutes" |
| 7 | Verify correct OTP also rejected | (actual code) | Still locked, 429 response |

---

## TC-011: Biometric Enrollment — Enable

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-008 |
| Preconditions | User just verified OTP, on biometric screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Observe screen | — | "Enable Face ID" heading, Enable and Skip buttons visible |
| 2 | Tap 'Enable Face ID' | — | OS biometric prompt appears |
| 3 | Authenticate biometric | (device biometric) | Success, navigates to location consent screen |
| 4 | Verify user record | — | biometric_enabled = true |

---

## TC-012: Biometric Enrollment — Skip

| Attribute | Value |
|---|---|
| Priority | Medium |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-008 |
| Preconditions | User on biometric screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Tap 'Skip for now' | — | Navigates to location consent screen |
| 2 | Verify user record | — | biometric_enabled = false |
| 3 | Verify future login | — | User will need password to log in |

---

## TC-013: Location Consent — Always

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, EP-01 |
| Linked Story | US-009 |
| Preconditions | User on location consent screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Observe screen | — | 3 options displayed: Always, Only when using app, Not now |
| 2 | Select 'Always' | — | Option highlighted with green radio |
| 3 | Tap 'Continue' | — | OS location permission dialog appears |
| 4 | Grant 'Always' permission | — | Navigates to transaction consent screen |
| 5 | Verify API call | POST /api/v1/auth/consent | 201 with consentType: 'location', decision: 'always' |

---

## TC-014: Location Consent — Not Now

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-009 |
| Preconditions | User on location consent screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Select 'Not now' | — | Option highlighted |
| 2 | Tap 'Continue' | — | No OS dialog, navigates to transaction consent |
| 3 | Verify consent recorded | — | decision: 'off' |
| 4 | Verify offer feed later | — | Shows all offers in city without proximity sorting |

---

## TC-015: Transaction Data Consent — Allow

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-010 |
| Preconditions | User on transaction consent screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Observe screen | — | Explanation of what's used/not used, Allow and No thanks buttons |
| 2 | Tap 'Allow' | — | Navigates to push permission screen |
| 3 | Verify API call | POST /api/v1/auth/consent | 201 with consentType: 'transaction', decision: 'allow' |

---

## TC-016: Transaction Data Consent — Decline

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-010 |
| Preconditions | User on transaction consent screen |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Tap 'No thanks' | — | Navigates to push permission screen |
| 2 | Verify consent recorded | — | decision: 'deny' |
| 3 | Verify personalization | — | Offers use category-only matching (no transaction data) |

---

## TC-017: Session Token Refresh (Silent)

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, EP-01 |
| Linked Story | US-011 |
| Preconditions | User authenticated, access token expired (>15min), refresh token valid |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Make API request with expired access token | GET /api/v1/users/me | — |
| 2 | Observe app behavior | — | No login prompt shown (silent refresh) |
| 3 | Verify refresh API called | POST /api/v1/auth/refresh | 200 with new accessToken and refreshToken |
| 4 | Verify original request retried | — | User data returned successfully |

---

## TC-018: Session Expiry (Refresh Token Expired)

| Attribute | Value |
|---|---|
| Priority | High |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-011 |
| Preconditions | Both access and refresh tokens expired |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Open app | — | Biometric prompt (or login screen if biometric disabled) |
| 2 | Verify refresh attempt | POST /api/v1/auth/refresh | 401 "Invalid refresh token" |
| 3 | Authenticate via biometric/password | — | New tokens issued, app loads normally |

---

## TC-019: Multi-Device Session

| Attribute | Value |
|---|---|
| Priority | Medium |
| Component | Auth |
| Labels | regression, EP-01 |
| Linked Story | US-012 |
| Preconditions | User logged in on Device A |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Log in on Device B | Same credentials | Success, both devices active |
| 2 | Use app on Device A | — | Still works (session independent) |
| 3 | Logout on Device A | POST /api/v1/auth/logout {deviceId: A} | Device A session ended |
| 4 | Use app on Device B | — | Still works (unaffected) |

---

## TC-020: Network Error During Registration

| Attribute | Value |
|---|---|
| Priority | Medium |
| Component | Auth |
| Labels | regression, negative, EP-01 |
| Linked Story | US-013 |
| Preconditions | App on registration screen, network disabled |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Enter valid phone | +447700900010 | — |
| 2 | Tap 'Continue' | — | After 10s timeout: "No internet connection — check your connection and try again" |
| 3 | Enable network | — | — |
| 4 | Tap 'Try again' (or 'Continue' again) | — | Request succeeds, OTP screen shown |

---

## TC-021: Complete Onboarding Happy Path (E2E)

| Attribute | Value |
|---|---|
| Priority | Critical |
| Component | Auth |
| Labels | regression, smoke, e2e, EP-01 |
| Linked Story | US-006, US-007, US-008, US-009, US-010 |
| Preconditions | Fresh install, no existing account |

| # | Action | Data | Expected Result |
|---|---|---|---|
| 1 | Enter phone | +447700900020 | — |
| 2 | Tap 'Continue' | — | OTP screen |
| 3 | Enter correct OTP | (from SMS) | Biometric screen |
| 4 | Tap 'Enable Face ID' | — | OS prompt → success → Location consent |
| 5 | Select 'Always', tap Continue | — | OS location dialog → grant → Transaction consent |
| 6 | Tap 'Allow' | — | Push permission screen |
| 7 | Tap 'Enable notifications' | — | OS push dialog → grant → Main app (Offers tab) |
| 8 | Verify final state | — | User verified, biometric enabled, location=always, transaction=allow, push=granted |
| 9 | Verify all consents via API | GET /api/v1/auth/consent | Both location and transaction consents recorded |

**Postconditions:** User fully onboarded, all permissions granted, on main app screen.
