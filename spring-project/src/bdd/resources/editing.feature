Feature: Restaurants editing

Scenario: Add new restaurant
	Given The User is on Home Page
	When The User navigates to "save" page
	And Enters restaurant name "new restaurant" and average price "10" and presses click
	Then A table must show the added restaurant with name "new restaurant", average price "10"

Scenario: Edit a non existing restaurant
	Given The User is on Home Page
	When The User navigates to "edit" page with id "-1"
	Then A message "No restaurant found with id: " + "-1" must be shown

Scenario: Edit an existing restaurant
	Given The User is on Home Page
	And The restaurant with id "10" exists in the database
	When  The User navigates to "edit" page with id "10"
	And Enters restaurant name "modified name" and average price "25" and presses click
	Then The User is redirected to Home Page
	And A table must show the modified restaurant "10 modified name 25"
