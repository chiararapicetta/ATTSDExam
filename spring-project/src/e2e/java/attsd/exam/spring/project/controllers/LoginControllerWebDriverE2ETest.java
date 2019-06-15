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
	

		private static int port =
			Integer.parseInt(System.getProperty("server.port", "8080"));

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
			driver.get(baseUrl+ "/signup");
			saveNewUser("giulia1@gmail", "pass", "giulia");
			assertThat(driver.getTitle()).contains("Login");
			assertEquals(1, urepository.count());

		}
		
		@Test
		public void loginWithParamError() throws Exception {
			driver.get(baseUrl+ "/signup");
			saveNewUser("francesco1@gmail", "password", "Francesco");
			driver.get(baseUrl+ "/signup");
			saveNewUser("francesco1@gmail", "password", "Francesco");
			assertEquals(driver.getTitle(), "Error");
		}
		
		@Test
		public void testLoginWithNoRestaurant() throws Exception {
			driver.get(baseUrl+ "/signup");
			saveNewUser("francesco@gmail", "password", "Francesco");
			login("francesco@gmail", "password");
			assertThat(driver.getPageSource()).contains("No restaurant");
		}
		
		@Test
		public void testLogout() throws Exception {
			driver.get(baseUrl+ "/signup");
			saveNewUser("francesco1@gmail", "password", "Francesco");
			login("francesco1@gmail", "password");
			driver.findElement
			(By.cssSelector("a[href*='/logout")).click();
			assertThat(driver.getPageSource()).contains("Login");
		}
		
		@Test
		public void testHomePageWithRestaurants() throws Exception {
			driver.get(baseUrl+ "/signup");
			saveNewUser("giada@gmail", "password", "Giada");
			login("giada@gmail", "password");
			driver.findElement(By.cssSelector("a[href*='/new")).click();
			driver.findElement(By.name("name")).sendKeys("Cacio e Pepe");
			driver.findElement(By.name("averagePrice")).sendKeys("20");
			driver.findElement(By.name("btn_submit")).click();
			assertThat(driver.findElement(By.id("restaurant_table")).getText()).
			contains("Cacio e Pepe", "20");
		}
		
		public void login(String email, String password) {
			driver.findElement(By.name("email")).sendKeys(email);
			driver.findElement(By.name("password")).sendKeys(password);
			driver.findElement(By.name("submit")).click();
		}
		
		
		public void saveNewUser(String email, String password, String username) {
			driver.findElement(By.name("email")).sendKeys(email);
			driver.findElement(By.name("password")).sendKeys(password);
			driver.findElement(By.name("username")).sendKeys(username);
			driver.findElement(By.name("submit")).click();

		}
}
/*

	@Test
	public void testEditNonExistentRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		EditPage page = EditPage.to(webDriver, BigInteger.valueOf(1));
		assertThat(page.getBody()).contains("No restaurant found with id: 1");
	}

	@Test
	public void testEditExistentRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		Restaurant r = new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34);
		restaurantService.storeInDb(r);
		EditPage page = EditPage.to(webDriver, BigInteger.valueOf(1));
		assertThat(page.getBody()).doesNotContain("No restaurant found with id: 1");
		HomePage homePage = page.submitForm(HomePage.class, "Pizzeria", 15);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 Pizzeria 15");
	}


	@Test
	public void testDeleteAllRestaurants() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "Pizzeria", 15));
		HomePage page = HomePage.toReset(webDriver);
		assertThat(page.getBody()).contains("No restaurant");
	}

	@Test
	public void testDeleteRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "Pizzeria", 15));
		HomePage page = HomePage.toDelete(webDriver, BigInteger.valueOf(1));
		assertThat(page.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n2 Pizzeria 15");
*/

