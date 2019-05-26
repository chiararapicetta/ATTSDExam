Feature: Restaurants listing and editing

Scenario: User Login and Logout
  Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	Given The User is on Login Page
	And load his email "email@gmail" and password "password"
	Then the user with email "email@gmail" is logged
	When The user logout
	Then he is redirect to LoginPage

Scenario: List no restaurants
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	Given The User is on Login Page
	And load his email "email@gmail" and password "password"
	When The User is on Home Page
	Given The database is empty
	Then A message "No restaurant" must be shown
	
Scenario: List current restaurants
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	Given The User is on Login Page
	And load his email "email@gmail" and password "password"
	Given Some restaurants are in the database
	When The User is on Home Page
	Then A table must show the restaurants

	