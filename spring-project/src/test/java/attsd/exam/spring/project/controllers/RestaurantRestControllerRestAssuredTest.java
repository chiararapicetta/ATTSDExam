package attsd.exam.spring.project.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

public class RestaurantRestControllerRestAssuredTest {
	private RestaurantService restaurantService;

	@Before
	public void setup() {
		restaurantService = mock(RestaurantService.class);
		standaloneSetup(new RestaurantRestController(restaurantService));
	}

	@Test
	public void testAllRestaurants() throws Exception {
		when(restaurantService.getAllRestaurants())
				.thenReturn(Arrays.asList
						(new Restaurant(BigInteger.valueOf(1), "Spera", 12), 
						new Restaurant(BigInteger.valueOf(2), "NimaSushi", 16)));
		given()
		.when()
		.get("/api/restaurants")
		.then()
		.statusCode(200)
		.assertThat()
		.body(
				"id[0]", equalTo(1), 
				"name[0]", equalTo("Spera"), 
				"averagePrice[0]", equalTo(12), 
				"id[1]", equalTo(2), 
				"name[1]", equalTo("NimaSushi"),
				"averagePrice[1]", equalTo(16));
		verify(restaurantService, times(1)).getAllRestaurants();
	}
}
