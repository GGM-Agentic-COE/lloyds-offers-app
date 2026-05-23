@epic-EP01 @regression
Feature: User Registration
  As a new consumer
  I want to register with my phone number or email
  So that I can create an account and access personalized offers

  Background:
    Given the auth-service API is running
    And the app is on the registration screen

  @story-US006 @critical @smoke
  Scenario: Successful registration with phone number
    Given no account exists for "+447700900001"
    When I enter "+447700900001" in the identifier field
    And I tap the "Continue" button
    Then the API responds with status 201
    And the response contains a userId
    And the response contains verificationRequired as true
    And I am navigated to the OTP verification screen

  @story-US006 @critical @smoke
  Scenario: Successful registration with email
    Given no account exists for "testuser@example.com"
    When I enter "testuser@example.com" in the identifier field
    And I tap the "Continue" button
    Then the API responds with status 201
    And I am navigated to the OTP verification screen

  @story-US006 @negative
  Scenario Outline: Registration with invalid identifier
    When I enter "<input>" in the identifier field
    Then the inline error "Please enter a valid phone number or email address" is displayed
    And the "Continue" button is disabled

    Examples:
      | input          |
      | 12345          |
      | abc@@          |
      | +1234          |
      | not-an-email   |
      |                |

  @story-US006 @negative
  Scenario: Registration with duplicate account
    Given an account already exists for "+447700900099"
    When I enter "+447700900099" in the identifier field
    And I tap the "Continue" button
    Then the API responds with status 409
    And the error message "An account with this already exists" is displayed
    And a "sign in" link is visible

  @story-US006 @security @boundary
  Scenario: Registration rate limiting (5 per device per hour)
    Given I have registered 5 accounts from this device in the last hour
    When I attempt to register a 6th account with "+447700900006"
    Then the API responds with status 429
    And the error message "Too many attempts" is displayed
    And the Retry-After header is present

  @story-US013 @negative
  Scenario: Registration with network timeout
    Given the network is unavailable
    When I enter "+447700900010" in the identifier field
    And I tap the "Continue" button
    And 10 seconds elapse
    Then the error message "No internet connection" is displayed
    And a "Try again" button is visible

  @story-US006 @accessibility
  Scenario: Registration screen accessibility
    Then all form fields have associated labels announced by screen reader
    And the "Continue" button has a minimum touch target of 44x44 points
    And error messages are announced via accessibility live region
