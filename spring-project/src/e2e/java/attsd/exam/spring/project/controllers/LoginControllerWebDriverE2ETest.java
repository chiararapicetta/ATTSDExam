package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.controllers.webdriver.pages.LoginPage;
import attsd.exam.spring.project.controllers.webdriver.pages.SignUpPage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;
import attsd.exam.spring.project.repositories.UserRepository;
import attsd.exam.spring.project.services.RestaurantService;

import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerWebDriverE2ETest {

	@Autowired
	private UserRepository urepository;

	@Autowired
	private RestaurantRepository repository;

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));

	private static String baseUrl = "http://localhost:" + port;

	private WebDriver driver;

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();
		urepository.deleteAll();
		repository.deleteAll();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testSignUp() {
		driver.get(baseUrl + "/signup");
		saveNewUser("giulia1@gmail", "pass", "giulia");
		assertThat(driver.getTitle()).contains("Login");
		assertEquals(1, urepository.count());

	}

	@Test
	public void loginWithParamError() throws Exception {
		driver.get(baseUrl + "/signup");
		saveNewUser("francesco1@gmail", "password", "Francesco");
		driver.get(baseUrl + "/signup");
		saveNewUser("francesco1@gmail", "password", "Francesco");
		assertThat(driver.getPageSource()).contains("ERROR");

	}

	@Test
	public void testLoginWithNoRestaurant() throws Exception {
		driver.get(baseUrl + "/signup");
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		assertThat(driver.getPageSource()).contains("No restaurant");
	}

	@Test
	public void testLogout() throws Exception {
		driver.get(baseUrl + "/signup");
		saveNewUser("francesco1@gmail", "password", "Francesco");
		login("francesco1@gmail", "password");
		driver.findElement(By.cssSelector("a[href*='/logout")).click();
		assertThat(driver.getPageSource()).contains("Login");
	}

	@Test
	public void testHomePageWithRestaurants() throws Exception {
		driver.get(baseUrl + "/signup");
		saveNewUser("giada@gmail", "password", "Giada");
		login("giada@gmail", "password");
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		driver.findElement(By.name("name")).sendKeys("Cacio e Pepe");
		driver.findElement(By.name("averagePrice")).sendKeys("20");
		driver.findElement(By.name("btn_submit")).click();
		assertThat(driver.findElement(By.id("restaurant_table")).getText()).contains("Cacio e Pepe", "20");
	}

	@Test
	public void testEditExistingRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		addOrEditRestaurant("Borgo al cotone", "40");
		driver.findElement(By.id("edit")).click();
		addOrEditRestaurant("Borgo al cotone", "35");
		assertThat(driver.findElement(By.id("restaurant_table")).getText()).contains("Borgo al cotone", "35");
	}

	@Test
	public void testDeleteAllRestaurants() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		addOrEditRestaurant("Borgo al cotone", "40");
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		addOrEditRestaurant("Scaraboci", "50");
		driver.findElement(By.cssSelector("a[href*='/reset")).click();
		assertThat(driver.getPageSource()).contains("No restaurant");
	}

	@Test
	public void testDeleteOneRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		driver.findElement(By.cssSelector("a[href*='/new")).click();
		addOrEditRestaurant("Borgo al cotone", "40");
		driver.findElement(By.id("delete")).click();
		assertThat(driver.getPageSource()).contains("No restaurant");
	}
	
	@Test
	public void testDeleteNotExistingRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		String falseId = "1";
		driver.get(baseUrl + "/delete?id"+falseId);
		assertThat(driver.getPageSource()).contains("Error");
	}
	
	@Test
	public void testEditNotExistingRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		String falseId = "1";
		driver.get(baseUrl + "/edit?id"+falseId);
		assertThat(driver.getPageSource()).contains("Error");
	}
	

	public void addOrEditRestaurant(String name, String averagePrice) {
		driver.findElement(By.name("name")).sendKeys(name);
		driver.findElement(By.name("averagePrice")).sendKeys(averagePrice);
		driver.findElement(By.name("btn_submit")).click();
	}

	public void login(String email, String password) {
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("submit")).click();
	}

	public void saveNewUser(String email, String password, String username) {
		driver.get(baseUrl + "/signup");
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("submit")).click();

	}
}
