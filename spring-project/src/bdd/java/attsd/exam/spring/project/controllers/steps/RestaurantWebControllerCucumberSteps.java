package attsd.exam.spring.project.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;

import attsd.exam.spring.project.controllers.webdriver.pages.AbstractPage;
import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;
import attsd.exam.spring.project.services.RestaurantService;
import attsd.exam.spring.project.services.UserService;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(loader = SpringBootContextLoader.class)
public class RestaurantWebControllerCucumberSteps {

	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired 
	private UserService userService;

	@Autowired
	private WebDriver webDriver;

	@LocalServerPort
	private int port;

	private HomePage homePage;

	private EditPage editPage;

	private AbstractPage redirectedPage;

	@Autowired
	private UserRepository userRepository;

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
		restaurantService.deleteAll();
		userRepository.deleteAll();
	}
	
	@After
	public void finish() {
		restaurantService.deleteAll();
	}
	
	@And("^The User is logged$")
	public void theUserIsLogged() throws Throwable {	
		webDriver.get("http://localhost:8080/login");
		webDriver.findElement(By.id("email")).sendKeys("USEREMAIL");
		webDriver.findElement(By.id("username")).sendKeys("USERNAME");
		webDriver.findElement(By.id("password")).sendKeys("USERPASSWORD");
		webDriver.findElement(By.id("Sign in")).click();
		assertEquals(1, userRepository.count());
		//assertThat(userService.loadUserByUsername(email)).isEqualTo(user.getEmail());
	}
	
	@Given("^The user is registered$")
	public void theUserIsRegistered() throws Throwable {
		User user = new User();
		user.setEmail("email@email");
		user.setPassword("userpassword");
		user.setUsername("username");
		userService.saveUser(user);
	}
	
	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		restaurantService.deleteAll();
	}

	@And("^The User is on Home Page$")
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
		assertThat(homePage.getRestaurantTableAsString())
				.isEqualTo("ID Name AveragePrice\n1 restaurant1 10\n2 restaurant2 20");
	}

	
	@When("^The User navigates to \"([^\"]*)\" page$")
	public void theUserNavigatesToPage(String newPage) throws Throwable {
		editPage = EditPage.to(webDriver);
	}

	@And("^Enters restaurant name \"([^\"]*)\" and average price \"([^\"]*)\" and presses click$")
	public void entersRestaurantNameAndPriceAndPressesClick(String name, String averagePrice) throws Throwable {
		redirectedPage = editPage.submitForm(HomePage.class, name, Integer.parseInt(averagePrice));
	}

	@Then("^The User is redirected to Home Page$")
	public void theUserIsRedirectedToHomePage() throws Throwable {
		assertThat(redirectedPage).isInstanceOf(HomePage.class);
	}

	@Then("^A table must show the added restaurant with name \"([^\"]*)\", average price \"([^\"]*)\"$")
	public void aTableMustShowTheAddedRestaurantWithNameAndAveragePrice(String name, int averagePrice)

			throws Throwable {
		assertThat(homePage.getRestaurantTableAsString()).matches("ID Name AveragePrice\n1 "+ name + " " + averagePrice);
	}

	@When("^The User navigates to \"([^\"]*)\" page with id \"([^\"]*)\"$")
	public void theUserNavigatesToPageWithId(String arg, String id) throws Throwable {
		editPage = EditPage.to(webDriver, BigInteger.valueOf(Long.parseLong(id)));
	}

	@Then("^A message \"([^\"]*)\" \\+ \"([^\"]*)\" must be shown$")
	public void aMessageMustBeShown(String messagePart, String id) throws Throwable {
		assertThat(editPage.getBody()).contains(messagePart + id);
	}

	@And("^The restaurant with id \"([^\"]*)\" exists in the database$")
	public void theEmployeeWithIdExistsInTheDatabase(String id) throws Throwable {
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(Long.parseLong(id)), "restaurant1", 25));
	}

	@And("^A table must show the modified restaurant \"([^\"]*)\"$")
	public void aTableMustShowTheModifiedrestaurant(String expectedRepresentation) throws Throwable {
		assertThat(homePage.getRestaurantTableAsString()).contains(expectedRepresentation);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n10 modified name 25");

	}
}
