package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.controllers.webdriver.pages.AbstractPage;
import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestaurantWebControllerWebDriverRealServerIT {

	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private WebDriver webDriver;
	@LocalServerPort
	private int port;

	@TestConfiguration
	static class WebDriverConfiguration {
		@Bean
		public WebDriver getWebDriver() {
			return new HtmlUnitDriver();
		}
	}

	@Before
	public void prepare() {
		AbstractPage.port = port;
		restaurantService.deleteAll();
	}

	@Test
	public void testHomePageWithNoRestaurants() throws Exception {
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getBody()).contains("No restaurant");
	}

	@Test
	public void testHomePageWithRestaurants() throws Exception {
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(2), "Pizzeria", 15));
		HomePage homePage = HomePage.to(webDriver);
		assertThat(homePage.getRestaurantTableAsString())
				.isEqualTo("ID Name AveragePrice\n1 CacioePepe 34\n2 Pizzeria 15");
	}
	/*
	 * @Test public void testEditNonExistentRestaurant() throws Exception { EditPage
	 * page = EditPage.to(webDriver, BigInteger.valueOf(1));
	 * assertThat(page.getBody()).contains("No restaurant found with id: 1");
	 * 
	 * }
	 */

	@Test
	public void testEditExistentRestaurant() throws Exception {
		restaurantService.storeInDb(new Restaurant(BigInteger.valueOf(1), "CacioePepe", 34));
		EditPage page = EditPage.to(webDriver, BigInteger.valueOf(1));
		assertThat(page.getBody()).doesNotContain("No restaurant found with id: 1");

		HomePage homePage = page.submitForm(HomePage.class, "Pizzeria", 15);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 Pizzeria 15");
	}

	/*
	@Test
	public void testNewRestaurant() throws Exception {
		EditPage page = EditPage.to(webDriver);
		HomePage homePage = page.submitForm(HomePage.class, "Scaraboci", 24);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 Scaraboci 24");
	}
*/
}
