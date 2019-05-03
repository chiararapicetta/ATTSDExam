Feature: Restaurants listing and editing

Scenario: List no restaurants
	Given The database is empty
	When The User is on Home Page
	Then A message "No restaurant" must be shown
	
Scenario: List current restaurants
	Given Some restaurants are in the database
	When The User is on Home Page
	Then A table must show the restaurants

