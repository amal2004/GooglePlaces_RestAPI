Feature: Place API
  As an API consumer
  I want to create, read, and delete places
  So that place records behave as expected

  @smoke @regression
  Scenario: Add a place successfully
    When I add a place named "QA House Alpha" in "English" at "101 Test Street"
    Then the HTTP status code is 200
    And the response field "status" equals "OK"
    And the response field "scope" equals "APP"
    And the response contains a place ID
    And the response content type is JSON

  @regression
  Scenario: Retrieve a newly created place and verify its name and address
    Given a place named "QA House Bravo" in "English" at "202 Test Avenue" has been created
    When I retrieve the created place
    Then the HTTP status code is 200
    And the retrieved place name is "QA House Bravo"
    And the retrieved place address is "202 Test Avenue"

  @regression
  Scenario: Delete a newly created place successfully
    Given a place named "QA House Charlie" in "English" at "303 Test Road" has been created
    When I delete the created place
    Then the HTTP status code is 200
    And the response field "status" equals "OK"

  # Five additional scenarios
  @regression @data-variation
  Scenario: Add a place using Spanish language and a different address
    When I add a place named "Casa QA Delta" in "Spanish" at "404 Quality Lane"
    Then the HTTP status code is 200
    And the response field "status" equals "OK"
    And the response contains a place ID

  @regression @data-variation
  Scenario: Retrieve a place and verify its name
    Given a place named "QA House Echo" in "English" at "505 Assertion Street" has been created
    When I retrieve the created place
    Then the HTTP status code is 200
    And the retrieved place name is "QA House Echo"

  @regression @data-variation
  Scenario: Retrieve a place and verify its address
    Given a place named "QA House Foxtrot" in "French" at "606 Validation Boulevard" has been created
    When I retrieve the created place
    Then the HTTP status code is 200
    And the retrieved place address is "606 Validation Boulevard"

  @regression @data-variation
  Scenario: Add a place with a longer name and confirm creation
    When I add a place named "QA Automation Training Center Golf" in "English" at "707 Framework Drive"
    Then the HTTP status code is 200
    And the response field "status" equals "OK"
    And the response contains a place ID

  @regression @lifecycle
  Scenario: Create and then delete a place
    Given a place named "QA House Hotel" in "English" at "808 Lifecycle Way" has been created
    When I delete the created place
    Then the HTTP status code is 200
    And the response field "status" equals "OK"
