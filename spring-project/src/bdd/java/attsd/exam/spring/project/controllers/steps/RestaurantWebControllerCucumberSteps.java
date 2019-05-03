package attsd.exam.spring.project.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import attsd.exam.spring.project.controllers.webdriver.pages.AbstractPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;
import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(loader = SpringBootContextLoader.class)
public class RestaurantWebControllerCucumberSteps {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private WebDriver webDriver;

	@LocalServerPort
	private int port;

	private HomePage homePage;

	static final Logger LOGGER = Logger.getLogger(RestaurantWebControllerCucumberSteps.class);

	@TestConfiguration
	static class WebDriverConfiguration {
		@Bean
		public WebDriver getWebDriver() {
			return new HtmlUnitDriver();
		}
	}

	@Before
	public void setup() {
		AbstractPage.port = port;
		LOGGER.info("Port set: " + port);
	}

	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		restaurantService.deleteAll();
	}

	@When("^The User is on Home Page$")
	public void the_User_is_on_Home_Page() throws Throwable {
		homePage = HomePage.to(webDriver);
	}

	@Then("^A message \"([^\"]*)\" must be shown$")
	public void a_message_must_be_shown(String expectedMessage) throws Throwable {
		assertThat(homePage.getBody()).contains(expectedMessage);
	}

	@Given("^Some restaurants are in the database$")
	public void someRestaurantsAreInTheDatabase() throws Throwable {
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "restaurant1", 10));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "restaurant2", 20));
	}

	@Then("^A table must show the restaurants$")
	public void aTableMustShowTheRestaurants() throws Throwable {
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 restaurant1 10\n2 restaurant2 20");
	}

}
