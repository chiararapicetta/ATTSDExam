package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantWebControllerWebDriverIT {

	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private WebDriver webDriver;

	@Before
	public void deleteAllEmployees() {
		restaurantService.deleteAll();
	}

	@Test
	public void testHomePageWithNoEmployees() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No restaurant");
	}
}
