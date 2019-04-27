package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestaurantWebController.class)
public class RestaurantWebControllerWebDriverTest {
	
	@MockBean
	private RestaurantService restaurantService;

	@Autowired
	private WebDriver webDriver;

	@Test
	public void testHomePageWithNoRestaurants() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No restaurant");
	}
}
