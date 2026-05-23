@epic-EP01 @regression
Feature: Session Management
  As an authenticated consumer
  I want my session to persist securely across app launches
  So that I don't have to log in every time

  Background:
    Given the auth-service API is running
    And a verified user exists with biometric enabled

  @story-US011 @critical @smoke
  Scenario: Silent token refresh
    Given the user's access token has expired
    And the refresh token is still valid
    When the app makes an API request
    Then the app silently calls POST /api/v1/auth/refresh
    And new access and refresh tokens are issued
    And the original request is retried successfully
    And no login prompt is shown to the user

  @story-US011 @high
  Scenario: Refresh token rotation (old token invalidated)
    Given the user has a valid refresh token "TOKEN_A"
    When the app refreshes using "TOKEN_A"
    Then a new refresh token "TOKEN_B" is issued
    And "TOKEN_A" is no longer valid
    When an attacker attempts to use "TOKEN_A"
    Then the API responds with status 401

  @story-US011 @high
  Scenario: Refresh token expired — re-authentication required
    Given both access and refresh tokens have expired
    When the user opens the app
    Then the biometric prompt is displayed
    When biometric succeeds
    Then new tokens are issued
    And the app loads normally

  @story-US011 @high
  Scenario: Logout invalidates device session
    Given the user is authenticated on device "DEVICE_A"
    When the user taps "Sign out" in settings
    Then POST /api/v1/auth/logout is called with deviceId "DEVICE_A"
    And the session for "DEVICE_A" is deleted server-side
    And the user is redirected to the registration screen
    And stored tokens are cleared from device secure storage

  @story-US012 @medium
  Scenario: Multi-device concurrent sessions
    Given the user is logged in on "DEVICE_A"
    When the user logs in on "DEVICE_B"
    Then both sessions are active independently
    When the user logs out on "DEVICE_A"
    Then "DEVICE_B" session remains active and unaffected

  @story-US012 @security
  Scenario: Password change invalidates all sessions
    Given the user is logged in on "DEVICE_A" and "DEVICE_B"
    When the user changes their password
    Then all sessions across all devices are invalidated
    And both devices require re-authentication

  @story-US011 @critical @smoke
  Scenario: Login with password (biometric skipped)
    Given a verified user who skipped biometric enrollment
    And the user has set a password
    When the user enters their identifier and password
    And taps "Sign in"
    Then POST /api/v1/auth/login returns 200 with tokens
    And the app loads the main screen

  @story-US011 @negative
  Scenario: Login with wrong password
    Given a verified user with password set
    When the user enters correct identifier but wrong password
    And taps "Sign in"
    Then the API responds with status 401
    And the error "Invalid credentials" is displayed
    And no information about whether the account exists is revealed

  @story-US011 @security
  Scenario: Login rate limiting
    Given a verified user
    When 5 failed login attempts are made within 15 minutes
    Then the 6th attempt returns 429
    And the account is temporarily locked
