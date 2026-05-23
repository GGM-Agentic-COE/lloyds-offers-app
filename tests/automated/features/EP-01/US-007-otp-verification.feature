@epic-EP01 @regression
Feature: OTP Verification
  As a new consumer who has entered my phone/email
  I want to verify my identity via a one-time code
  So that my account is secured against unauthorized creation

  Background:
    Given the auth-service API is running
    And a user has registered with "+447700900001" and status is "pending"
    And an OTP has been sent

  @story-US007 @critical @smoke
  Scenario: Successful OTP verification
    Given I am on the OTP verification screen
    When I enter the correct 6-digit OTP
    And I tap the "Verify" button
    Then the API responds with status 200
    And the response contains an accessToken
    And the response contains a refreshToken
    And the response contains expiresIn as 900
    And the user status is updated to "verified"
    And I am navigated to the biometric enrollment screen

  @story-US007 @negative
  Scenario: OTP verification with wrong code
    Given I am on the OTP verification screen
    When I enter "000000" as the OTP
    And I tap the "Verify" button
    Then the API responds with status 401
    And the error message "Invalid code — 4 attempts remaining" is displayed
    And the OTP input fields are cleared

  @story-US007 @boundary
  Scenario: OTP expires after 90 seconds
    Given I am on the OTP verification screen
    When 90 seconds elapse without entering the code
    And I enter the original OTP
    And I tap the "Verify" button
    Then the API responds with status 401
    And the error message "Code expired" is displayed
    And a "Resend code" button is visible

  @story-US007 @security
  Scenario: OTP lockout after 5 failed attempts
    Given I am on the OTP verification screen
    When I enter an incorrect OTP 5 times
    Then the account is locked for 30 minutes
    And the error message "Too many attempts. Try again in 30 minutes" is displayed
    And even the correct OTP is rejected during lockout

  @story-US007 @security
  Scenario: OTP resend rate limiting (3 per 10 minutes)
    Given I am on the OTP verification screen
    When I tap "Resend code" 3 times within 10 minutes
    And I tap "Resend code" a 4th time
    Then the API responds with status 429
    And the error message "Too many requests — try again in 10 minutes" is displayed

  @story-US007 @accessibility
  Scenario: OTP screen accessibility
    Given I am on the OTP verification screen
    Then the OTP input auto-focuses on the first box
    And the countdown timer is announced by screen reader
    And the "Resend code" button has minimum 44x44 touch target
