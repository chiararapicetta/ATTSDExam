Feature: Restaurants editing


Scenario: List no restaurants
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	Then The User is on Login Page
	When The User load his email "email@gmail" and password "password"
	Then The message "No restaurants" must be shown

Scenario: Add and Edit a new restaurant
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	And The User load his email "email@gmail" and password "password"
	And The User clicks to add a new restaurant
	And Enters a restaurant with name "Antichi Sapori" and average price "25"
	Then The table must show the restaurant with name "Antichi Sapori" and average price "25"
	And The buttons "Edit" and "Delete" are displayed
	When The User clicks to "edit" the restaurant
	And updates the average price to "30"
	Then The table must show the restaurant with name "Antichi Sapori" and average price "30"
	
	Scenario: Add and Delete a restaurant
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	And The User load his email "email@gmail" and password "password"
	And The User clicks to add a new restaurant
	And Enters a restaurant with name "Yoko" and average price "30"
	Then The table must show the restaurant with name "Yoko" and average price "30"
	And The buttons "Edit" and "Delete" are displayed
	When The User clicks to "delete" the restaurant
	Then The message "No restaurants" must be shown
	
Scenario: Reset the restaurant database
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	And The User load his email "email@gmail" and password "password"
	And The User clicks to add a new restaurant
	And Enters a restaurant with name "Yoko" and average price "30"
	When The User clicks to reset link to delete all restaurants
	Then The message "No restaurants" must be shown
	
	Scenario: Error Pages
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	And The User load his email "email@gmail" and password "password"
	When The User tries to "edit" a not existing restaurant
	Then The message "Error" must be shown
	Given The User is on HomePage
	When The User tries to "delete" a not existing restaurant
	Then The message "Error" must be shown
	
	
	
	
	
	
	
	
	
	
	
	
