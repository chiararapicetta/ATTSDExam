package attsd.exam.spring.project.controllers;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/e2e/resources/authentication.feature")
public class RestaurantsAuthenticationE2ETest {

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
}
}