package attsd.exam.spring.project.controllers.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

import attsd.exam.spring.project.repositories.RestaurantRepository;
import attsd.exam.spring.project.repositories.UserRepository;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(loader = SpringBootContextLoader.class)
public class RestaurantWebControllerSteps {


	@Autowired
	private UserRepository urepository;
	
	@Autowired
	RestaurantRepository restaurantRepository;
	
	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	
	private static String baseUrl = "http://localhost:" + port;
	

	private WebDriver driver;


	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
		urepository.deleteAll();
		restaurantRepository.deleteAll();
	}

	@After
	public void teardown() {
		driver.quit();
	}



	@Given("^The User is on SignUp Page$")
	public void the_User_is_on_SignUp_Page() throws Throwable {
		driver.get(baseUrl + "/signup");
	}

	@When("^Enters email \"([^\"]*)\" , password \"([^\"]*)\" and username \"([^\"]*)\"$")
	public void theUserIsLogged(String email, String password, String username) throws Throwable {
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("submit")).click();
	}

	@Then("^The User is on Login Page$")
	public void the_User_is_on_Login_Page() throws Throwable {
		assertThat(driver.getTitle()).contains("Login");
	}

	@When("^The User load his email \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void load_his_email_and_password(String email, String password) throws Throwable {
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("submit")).click();
	}

	@Then("^The user with email \"([^\"]*)\" is logged$")
	public void the_user_with_email_is_logged(String email) throws Throwable {
		assertThat(driver.getTitle()).contains("Restaurants");
	}

	@When("^The user logout$")
	public void the_User_make_logout() throws Throwable {
		driver.get(baseUrl + "/logout");
	}
	
	@Then("^The message \"([^\"]*)\" must be shown$")
	public void a_message_must_be_shown(String expectedMessage) throws Throwable {
		assertThat(driver.getPageSource()).contains(expectedMessage);
	}


	@When("^The User clicks to add a new restaurant$")
	public void the_User_click_to_add_a_new_restaurant() throws Throwable {
		driver.findElement(By.cssSelector("a[href*='/new")).click();
	}
	
	@When("^Enters a restaurant with name \"([^\"]*)\" and average price \"([^\"]*)\"$")
	public void enters_a_restaurant_with_name_and_average_price(String name, String averagePrice) throws Throwable {
		driver.findElement(By.name("name")).sendKeys(name);
		driver.findElement(By.name("averagePrice")).sendKeys(averagePrice);
		driver.findElement(By.name("btn_submit")).click();
	}


	@Then("^The table must show the restaurant with name \"([^\"]*)\" and average price \"([^\"]*)\"$")
	public void the_table_must_show_the_restaurant_with_name_and_average_price(String name, String averagePrice) throws Throwable {
		assertThat(driver.findElement(By.id("restaurant_table")).getText()).contains(name+" "+averagePrice+" Edit Delete");
	}
	
	@Then("^The buttons \"([^\"]*)\" and \"([^\"]*)\" are displayed$")
	public void the_buttons_and_are_displayed(String buttonOne, String buttonTwo) throws Throwable {
		WebElement editButton = driver.findElement(By.id("edit"));
		assertEquals(buttonOne, editButton.getText());
		WebElement deleteButton = driver.findElement(By.id("delete"));
		assertEquals(buttonTwo, deleteButton.getText());
		assertTrue(editButton.isDisplayed());
		assertTrue(editButton.isEnabled());
		assertTrue(deleteButton.isDisplayed());
		assertTrue(deleteButton.isEnabled());
	}
	
	@When("^The User clicks to \"([^\"]*)\" the restaurant$")
	public void the_User_clicks_to_edit_the_restaurant(String button) throws Throwable {
		driver.findElement(By.id(button)).click();
	}


	@When("^updates the average price to \"([^\"]*)\"$")
	public void updates_the_average_price_to(String aprice) throws Throwable {
		driver.findElement(By.name("averagePrice")).clear();
		driver.findElement(By.name("averagePrice")).sendKeys(aprice);
		driver.findElement(By.name("btn_submit")).click();
	}
	
	@When("^The User clicks to reset link to delete all restaurants$")
	public void the_User_click_to_reset_link_to_delete_all_restaurants() throws Throwable {
		driver.findElement(By.cssSelector("a[href*='/reset")).click();
	}
	
	@When("^The User tries to \"([^\"]*)\" a not existing restaurant$")
	public void the_User_tries_to_edit_a_not_existing_restaurant(String name) throws Throwable {
		driver.get(baseUrl + "/"+name+"?id=10");
	}
	
	@Given("^The User is on HomePage$")
	public void the_User_is_on_HomePage() throws Throwable {
		driver.get(baseUrl);
	}
}