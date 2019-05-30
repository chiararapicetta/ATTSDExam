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

import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.controllers.webdriver.pages.LoginPage;
import attsd.exam.spring.project.controllers.webdriver.pages.SignUpPage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;
import attsd.exam.spring.project.services.RestaurantService;
import attsd.exam.spring.project.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerWebDriverIT {

	@Autowired
	private UserService service;
	
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
	public void testGetLogin() throws Exception {
		User u = new User();
		u.setEmail("francesco@gmail");
		u.setPassword("password");
		u.setUsername("Francesco");
		service.saveUser(u);
		assertEquals(1, urepository.count());
		LoginPage page = LoginPage.to(webDriver);
		page.submitForm(LoginPage.class, "francesco@gmail", "password");
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "Pizzeria", 15));
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getRestaurantTableAsString())
				.isEqualTo("ID Name AveragePrice\n1 CacioePepe 34\n2 Pizzeria 15");
		
	}
	
	@Test
	public void test() throws Exception {
		User u = new User();
		u.setEmail("alice@gmail");
		u.setPassword("password");
		u.setUsername("Alice");
		service.saveUser(u);
		SignUpPage page = SignUpPage.to(webDriver);
		page.submitForm(SignUpPage.class, "alice@gmail", "password", "Alice");
		assertThat(page.getBody()).contains("ERROR\nSomething wrong\nClick here to retry authentication.\nClick here if you already logged.");
	}

}
