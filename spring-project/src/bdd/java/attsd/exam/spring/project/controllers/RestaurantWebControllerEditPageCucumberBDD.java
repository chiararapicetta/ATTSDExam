package attsd.exam.spring.project.controllers;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/bdd/resources/editing.feature")
public class RestaurantWebControllerEditPageCucumberBDD {

}