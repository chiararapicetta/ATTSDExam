Feature: Restaurants editing

Scenario: Add a new User
	Given The User is on SignUp Page
	When Enters email "email@gmail" , password "password" and username "user" 
	Then The User is on Login Page
	And load his email "email@gmail" and password "password" 
	
