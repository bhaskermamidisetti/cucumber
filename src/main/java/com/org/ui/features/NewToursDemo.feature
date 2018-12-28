@Demo 
Feature: This is test feature 

@sampleTestCase @Demo 
Scenario: scenario one 
	Given I am at home page of the application
	And I enter "demo" into userName and "demo" into password 
	When I click on Login button 
	Then I should see SignOff link 
	
	
@myTestCase @Demo 
Scenario: scenario two 
	Given I am at home page of the application
	And I enter "demo" into userName and "demo" into password 
	When I click on Login button 
	Then I should see SignOff link 
