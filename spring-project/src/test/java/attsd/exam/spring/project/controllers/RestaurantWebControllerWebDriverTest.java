package attsd.exam.spring.project.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.controllers.webdriver.pages.EditPage;
import attsd.exam.spring.project.controllers.webdriver.pages.HomePage;
import attsd.exam.spring.project.model.Restaurant;
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

	@Test
	public void testHomePageWithRestaurants() throws Exception {
		when(restaurantService.getAllRestaurants())
				.thenReturn(Arrays.asList(new Restaurant(BigInteger.valueOf(1), "DaZorba", 25),
						new Restaurant(BigInteger.valueOf(2), "Publius", 40)));
		HomePage homePage = HomePage.to(webDriver);

		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 DaZorba 25\n2 Publius 40");
	}

	@Test
	public void testEditNonExistentRestaurant() throws Exception {
		EditPage page = EditPage.to(webDriver, BigInteger.valueOf(1));
		assertThat(page.getBody()).contains("No restaurant found with id: 1");
	}

	
	@Test
	public void testEditExistentRestaurant() throws Exception {
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1)))
				.thenReturn(new Restaurant(BigInteger.valueOf(1), "LaCostaDelMancino", 45));
		
		Restaurant expectedSave = new Restaurant(BigInteger.valueOf(1), "IlGalloNero", 35);
		
		when(restaurantService.getAllRestaurants()).thenReturn(Arrays.asList(expectedSave));
		
		EditPage page = EditPage.to(webDriver, BigInteger.valueOf(1));
		assertThat(page.getBody()).doesNotContain("No restaurant found with id: 1");
		HomePage homePage = page.submitForm(HomePage.class, "IlGalloNero", 35);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 IlGalloNero 35");
		verify(restaurantService).storeInDb(expectedSave);
	}

	@Test
	public void testNewRestaurant() throws Exception {
		Restaurant expectedSave = new Restaurant(BigInteger.valueOf(1), "OsteriaDelNoce", 40);
		when(restaurantService.getAllRestaurants()).thenReturn(Arrays.asList(expectedSave));
		EditPage page = EditPage.to(webDriver);
		HomePage homePage = page.submitForm(HomePage.class, "OsteriaDelNoce", 40);
		assertThat(homePage.getRestaurantTableAsString()).isEqualTo("ID Name AveragePrice\n1 OsteriaDelNoce 40");
		verify(restaurantService).storeInDb(expectedSave);
	}
}
