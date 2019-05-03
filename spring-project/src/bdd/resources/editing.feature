Feature: Restaurants editing

Scenario: Add new restaurant
	Given The user is on Home Page
	When The User navigates to "save" page
	And Enters restaurant name "new restaurant" and average price "10" and press click
	Then The User is redirected to Home Page
	And A table must show the added restaurant with name "new restaurant", average price "10" and id is positive