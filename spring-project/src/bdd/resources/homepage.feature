Feature: Restaurants listing and editing

Scenario: List no restaurants
	Given The database is empty
	When The User is on Home Page
	Then A message "No restaurants" must be shown