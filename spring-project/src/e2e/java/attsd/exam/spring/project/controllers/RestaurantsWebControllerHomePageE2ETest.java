package attsd.exam.spring.project.controllers;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/e2e/resources/homepage.feature")
public class RestaurantsWebControllerHomePageE2ETest {

}