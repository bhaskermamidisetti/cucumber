@DemoFlipkart
Feature: Validate search item functionality in Flipkart

  @flipkartTestCase @DemoFlipkart
  Scenario: Validate search functionality
    Given I am at home page of the application
    And I clicked on search products field
    When I entered any product name in search box
    And I clicked on Search icon
    Then I should see list of products
