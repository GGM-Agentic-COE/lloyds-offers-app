@epic-EP01 @regression
Feature: Consent Collection
  As a new consumer completing registration
  I want to choose my privacy preferences for location and transaction data
  So that I maintain control over my personal information

  Background:
    Given the auth-service API is running
    And the user has completed biometric enrollment
    And the user is authenticated with a valid JWT

  @story-US009 @critical @smoke
  Scenario: Location consent — Always
    Given I am on the location consent screen
    When I select the "Always" option
    And I tap "Continue"
    Then the OS location permission dialog is triggered
    When I grant "Always" permission
    Then the API records consent with type "location" and decision "always"
    And I am navigated to the transaction consent screen

  @story-US009 @high
  Scenario: Location consent — App open only
    Given I am on the location consent screen
    When I select "Only when using the app"
    And I tap "Continue"
    Then the OS location permission dialog is triggered for "When In Use"
    When I grant permission
    Then the API records consent with type "location" and decision "app_open"

  @story-US009 @high
  Scenario: Location consent — Not now
    Given I am on the location consent screen
    When I select "Not now"
    And I tap "Continue"
    Then no OS permission dialog is shown
    And the API records consent with type "location" and decision "off"
    And I am navigated to the transaction consent screen

  @story-US009 @negative
  Scenario: Location consent — OS permission denied
    Given I am on the location consent screen
    When I select "Always"
    And I tap "Continue"
    And I deny the OS location permission
    Then the consent falls back to decision "off"
    And I am navigated to the transaction consent screen

  @story-US010 @high
  Scenario: Transaction consent — Allow
    Given I am on the transaction consent screen
    When I tap "Allow"
    Then the API records consent with type "transaction" and decision "allow"
    And I am navigated to the push permission screen

  @story-US010 @high
  Scenario: Transaction consent — Decline
    Given I am on the transaction consent screen
    When I tap "No thanks"
    Then the API records consent with type "transaction" and decision "deny"
    And I am navigated to the push permission screen

  @story-US009 @story-US010 @security
  Scenario: Consent records are immutable
    Given the user has recorded location consent as "always"
    When the user changes location consent to "off"
    Then a new consent record is created
    And the previous consent has superseded_at set
    And the GET consent API returns only the latest decision

  @story-US009 @accessibility
  Scenario: Consent screen accessibility
    Given I am on the location consent screen
    Then each option announces its label and description via screen reader
    And the selected option announces "selected" state
    And the privacy policy link is accessible
