package attsd.exam.spring.project.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;
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
				.thenReturn(Arrays.asList(new Restaurant(BigInteger.valueOf(1), "Spera", 12),
						new Restaurant(BigInteger.valueOf(2), "NimaSushi", 16)));
		given().when().get("/api/restaurants").then().statusCode(200).assertThat().body("id[0]", equalTo(1), "name[0]",
				equalTo("Spera"), "averagePrice[0]", equalTo(12), "id[1]", equalTo(2), "name[1]", equalTo("NimaSushi"),
				"averagePrice[1]", equalTo(16));
		verify(restaurantService, times(1)).getAllRestaurants();
	}

	@Test
	public void testFindByIdWithExistingRestaurant() throws Exception {
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1)))
				.thenReturn(new Restaurant(BigInteger.valueOf(1), "VinoERavioli", 15));
		given().when().get("/api/restaurants/1").then().statusCode(200).assertThat().body("id", equalTo(1), "name",
				equalTo("VinoERavioli"), "averagePrice", equalTo(15));
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	public void testFindByIdWithNonExistingRestaurant() throws Exception {
		given().when().get("/api/restaurants/1").then().statusCode(200).contentType(isEmptyOrNullString());
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	public void testNewRestaurant() throws Exception {
		Restaurant r = new Restaurant(null, "Pizzeria", 13);
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(r).when().post("/api/restaurants/new").then()
				.statusCode(200);
		verify(restaurantService, times(1)).storeInDb(r);

	}

	@Test
	public void testUpdateRestaurant() throws Exception {  
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1)))
				.thenReturn(new Restaurant(BigInteger.valueOf(1), "Yoko", 13));
		
		Restaurant updated = new Restaurant(BigInteger.valueOf(1), "Kyoto", 18);
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when().put("/api/restaurants/update/1")
				.then().statusCode(200);
		verify(restaurantService, times(1)).storeInDb(updated);

	}

	@Test
	public void testUpdateRestaurantWithFakeId() throws Exception {
	
		Restaurant updated = new Restaurant(BigInteger.valueOf(1), "PesceFresco", 35);
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when().put("/api/restaurants/update/1")
				.then().statusCode(200);
		verify(restaurantService, times(1)).storeInDb(updated);

	}

	@Test
	public void testDeleteRestaurant() throws Exception {
		Restaurant r = new Restaurant(BigInteger.valueOf(1), "VinoERavioli", 15);
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1)))
		.thenReturn(r);
		given().when().delete("/api/restaurants/delete/1").then().statusCode(200);
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
		verify(restaurantService, times(1)).delete(r);

	}
	
	@Test
	public void testDeleteRestaurantNotExists() throws Exception {
		Restaurant r = new Restaurant();
		given().when().delete("/api/restaurants/delete/1").then().statusCode(200);
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
		verify(restaurantService, times(0)).delete(r);
	}

}
