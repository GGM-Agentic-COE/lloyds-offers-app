@epic-EP01 @regression @e2e
Feature: Complete Onboarding Journey (End-to-End)
  As a new consumer
  I want to complete the full registration flow
  So that I have a fully configured account ready to receive offers

  @story-US006 @story-US007 @story-US008 @story-US009 @story-US010 @critical @smoke
  Scenario: Full happy path — phone registration to main app
    Given the auth-service API is running
    And no account exists for "+447700900020"
    # Registration
    When I enter "+447700900020" on the registration screen
    And I tap "Continue"
    Then I am on the OTP verification screen
    # OTP
    When I enter the correct OTP received via SMS
    And I tap "Verify"
    Then I am on the biometric enrollment screen
    # Biometric
    When I tap "Enable Face ID"
    And the OS biometric prompt succeeds
    Then I am on the location consent screen
    # Location consent
    When I select "Always" and tap "Continue"
    And I grant the OS location permission
    Then I am on the transaction consent screen
    # Transaction consent
    When I tap "Allow"
    Then I am on the push permission screen
    # Push permission
    When I tap "Enable notifications"
    And I grant the OS push permission
    Then I am on the main app Offers tab
    # Verify final state
    And the user status is "verified"
    And biometric_enabled is true
    And location consent is "always"
    And transaction consent is "allow"
    And a device token is registered for push notifications

  @story-US006 @story-US007 @story-US009 @story-US010 @high
  Scenario: Minimal path — skip biometric, decline all permissions
    Given no account exists for "minimal@example.com"
    When I register with "minimal@example.com"
    And I verify the OTP
    And I tap "Skip for now" on biometric
    And I select "Not now" on location consent and tap "Continue"
    And I tap "No thanks" on transaction consent
    And I tap "Maybe later" on push permission
    Then I am on the main app Offers tab
    And biometric_enabled is false
    And location consent is "off"
    And transaction consent is "deny"
    And no device token is registered for push
    And the offers tab shows "No offers yet" empty state
