package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.controllers.webdriver.pages.LoginPage;
import attsd.exam.spring.project.controllers.webdriver.pages.SignUpPage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.UserRepository;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerWebDriverIT {


	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private UserRepository urepository;

	@Autowired
	private WebDriver webDriver;

	@Before
	public void deleteAllRestaurants() {
		restaurantService.deleteAll();
		urepository.deleteAll();
	}

	@Test
	public void testSignUp() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		assertEquals(1, urepository.count());
	}
	
	@Test
	public void testLogout() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		assertEquals(1, urepository.count());
		HomePage page = HomePage.toLogout(webDriver);
		assertThat(page.getBody()).contains("Please sign in");
	}
	

	@Test
	public void testSaveUserAlreadyExists() throws Exception {
		saveNewUser("alice@gmail", "passAlice", "Alice");
		SignUpPage page = SignUpPage.to(webDriver);
		page.submitForm(SignUpPage.class, "alice@gmail", "passAlice", "Alice");
		assertThat(page.getBody()).contains(
				"ERROR\nSomething wrong\nClick here to retry authentication.\nClick here if you already logged.");
	}

	@Test
	public void testHomePageWithNoRestaurants() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No restaurant");
	}

	@Test
	public void testHomePageWithRestaurants() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "Pizzeria", 15));
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getRestaurantTableAsString())
				.isEqualTo("ID Name AveragePrice\n1 CacioePepe 34\n2 Pizzeria 15");

	}

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
	public void testNewRestaurant() throws Exception {
		saveNewUser("francesco@gmail", "password", "Francesco");
		login("francesco@gmail", "password");
		EditPage page = EditPage.to(webDriver);
		HomePage homePage = page.submitForm(HomePage.class, "Scaraboci", 24);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 Scaraboci 24");
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
	
	public void saveNewUser(String email, String password, String username) {
		SignUpPage page = SignUpPage.to(webDriver);
		page.submitForm(SignUpPage.class, email, password, username);
	}

	public void login(String email, String password) {
		LoginPage page = LoginPage.to(webDriver);
		page.submitForm(LoginPage.class, email, password);
	}

}
