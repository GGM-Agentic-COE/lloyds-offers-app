@epic-EP01 @regression
Feature: Biometric Enrollment
  As a verified consumer
  I want to enable Face ID or fingerprint login
  So that I can access the app quickly without entering credentials

  Background:
    Given the auth-service API is running
    And the user has completed OTP verification
    And I am on the biometric enrollment screen

  @story-US008 @high
  Scenario: Enable biometric authentication
    When I tap "Enable Face ID"
    And the OS biometric prompt succeeds
    Then the user record has biometric_enabled = true
    And the refresh token is stored in secure enclave
    And I am navigated to the location consent screen

  @story-US008 @high
  Scenario: Skip biometric enrollment
    When I tap "Skip for now"
    Then the user record has biometric_enabled = false
    And I am navigated to the location consent screen

  @story-US008 @negative
  Scenario: Biometric fails 3 times
    When the OS biometric prompt fails 3 consecutive times
    Then the app falls back to password entry
    And a "Set up password" screen is displayed

  @story-US008 @accessibility
  Scenario: Biometric screen accessibility
    Then the screen purpose is announced by screen reader
    And both "Enable" and "Skip" buttons have minimum 44x44 touch targets
