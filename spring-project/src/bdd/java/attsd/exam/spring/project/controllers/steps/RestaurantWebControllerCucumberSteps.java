package attsd.exam.spring.project.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.junit.After;
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
import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.controllers.webdriver.pages.LoginPage;
import attsd.exam.spring.project.controllers.webdriver.pages.SignUpPage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;
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
	
	@Autowired
	private RestaurantRepository restaurantRepository;

	@LocalServerPort
	private int port;

	private HomePage homePage;

	private SignUpPage signUpPage;

	private EditPage editPage;

	private AbstractPage redirectedPage;

	private LoginPage loginPage;

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

	@Given("^The User is on SignUp Page$")
	public void the_User_is_on_SignUp_Page() throws Throwable {
		signUpPage = SignUpPage.to(webDriver);
	}

	@When("^Enters email \"([^\"]*)\" , password \"([^\"]*)\" and username \"([^\"]*)\"$")
	public void theUserIsLogged(String email, String password, String username) throws Throwable {
		redirectedPage = signUpPage.submitForm(SignUpPage.class, email, password, username);
	}

	@Given("^The User is on Login Page$")
	public void the_User_is_on_Login_Page() throws Throwable {
		loginPage = LoginPage.to(webDriver);
	}

	@And("^load his email \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void load_his_email_and_password(String email, String password) throws Throwable {
		redirectedPage = loginPage.submitForm(LoginPage.class, email, password);
	}

	@Then("^the user with email \"([^\"]*)\" is logged$")
	public void the_user_with_email_is_logged(String email) throws Throwable {
		assertThat(userService.loadUserByUsername(email).isEnabled()).isTrue();
	}

	@When("^The user logout$")
	public void the_User_make_logout() throws Throwable {
		homePage = HomePage.toLogout(webDriver);
	}

	@Then("^he is redirect to LoginPage$")
	public void he_is_redirect_to_LoginPage() throws Throwable {
		assertThat(redirectedPage).isInstanceOf(LoginPage.class);
	}

	@When("^The User is on Home Page$")
	public void the_User_is_on_Home_Page() throws Throwable {
		homePage = HomePage.to(webDriver);
	}

	@Given("^The database is empty$")
	public void the_database_is_empty() throws Throwable {
		restaurantService.deleteAll();
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

	@When("^The User navigates to new page$")
	public void theUserNavigatesToNewPage() throws Throwable {
		editPage = EditPage.to(webDriver);
	}

	@And("^Enters restaurant name \"([^\"]*)\" and average price \"([^\"]*)\" and presses click$")
	public void entersRestaurantNameAndPriceAndPressesClick(String name, String averagePrice) throws Throwable {
		redirectedPage = editPage.submitForm(HomePage.class, name, Integer.parseInt(averagePrice));
	}

	@Then("^A table must show the added restaurant with name \"([^\"]*)\", average price \"([^\"]*)\"$")
	public void aTableMustShowTheAddedRestaurantWithNameAndAveragePrice(String name, int averagePrice)
			throws Throwable {
		assertThat(homePage.getRestaurantTableAsString()).contains( name + " " + averagePrice);
	}

	@When("^The User navigates to edit page with id \"([^\"]*)\"$")
	public void theUserNavigatesToEditPageWithId(String id) throws Throwable {
			editPage = EditPage.to(webDriver, BigInteger.valueOf(Long.parseLong(id)));
	}
	
	@When("^The User navigates to delete page with id \"([^\"]*)\"$")
	public void theUserNavigatesToDeletePageWithId(String id) throws Throwable {
			homePage = HomePage.toDelete(webDriver, BigInteger.valueOf(Long.parseLong(id)));
	}

	@Then("^A message \"([^\"]*)\" \\+ \"([^\"]*)\" must be shown$")
	public void aMessageMustBeShown(String messagePart, String id) throws Throwable {
		assertThat(editPage.getBody()).contains(messagePart + id);
	}

	@And("^The restaurant with id \"([^\"]*)\" exists in the database$")
	public void theRestaurantWithIdExistsInTheDatabase(String id) throws Throwable {
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(Long.parseLong(id)), "restaurant1", 25));
		assertNotNull(restaurantService.getRestaurantById(BigInteger.valueOf(Long.parseLong(id))));
	}

	@Then("^The User is redirected to Home Page$")
	public void theUserIsRedirectedToHomePage() throws Throwable {
		assertThat(redirectedPage).isInstanceOf(HomePage.class);
	}

	@And("^A table must show the modified restaurant \"([^\"]*)\"$")
	public void aTableMustShowTheModifiedrestaurant(String expectedRepresentation) throws Throwable {
		assertThat(homePage.getRestaurantTableAsString()).contains(expectedRepresentation);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n10 modified name 25");
	}

	@Then("^The restaurant with id \"([^\"]*)\" doesn't exists in the database$")
	public void theRestaurantWithIdDoesntExistsInTheDatabase(String id) throws Throwable {
		assertNull(restaurantService.getRestaurantById(BigInteger.valueOf(Long.parseLong(id))));
	}
	
	@When("^The User navigates to reset page$")
	public void theUserNavigatesToResetPage() throws Throwable {
		homePage = HomePage.toReset(webDriver);
	}

	@Then("^The message \"([^\"]*)\" must be shown$")
	public void theMessageMustBeShown(String message) throws Throwable {
		assertThat(loginPage.getBody()).contains(message);
	}

	@Then("^The error message \"([^\"]*)\" must be shown$")
	public void theErrorMessageMustBeShown(String message) throws Throwable {
		assertThat(signUpPage.getBody()).contains(message);
	}

}
