package attsd.exam.spring.project.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.log4j.Logger;


import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestaurantRestControllerIT {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@LocalServerPort
	private int port;

	private String url;

	static final Logger LOGGER = Logger.getLogger(RestaurantRestControllerIT.class);

	@Before
	public void setup() {
		url = "http://localhost:" + port;
		restaurantRepository.deleteAll();
		// restaurantRepository.flush();
		//showCurrentRestaurantsOnTheLog();
	}

	/*
	 * private void showCurrentRestaurantsOnTheLog() { LOGGER.info("restaurants: " +
	 * restaurantRepository.findAll()); }
	 */
	
	@After
	public void reset() {
		restaurantRepository.deleteAll();
	}

	@Test
	public void testAllRestaurantsWithNoRestaurant() throws Exception {
		given().when().get(url + "/api/restaurants").then().statusCode(200).assertThat().body(is("[]"));
	}
	
	@Test
	public void testAllRestaurants() throws Exception {
		List<Restaurant> saved = restaurantRepository.saveAll(Arrays.asList(new Restaurant(null, "Il Capriccio", 20), new Restaurant(null, "Seasons", 16)));
		//restaurantRepository.flush();
		//showCurrentRestaurantsOnTheLog();
		given().when().get(url + "/api/restaurants").then().statusCode(200).assertThat().body("name[0]",
				equalTo("Il Capriccio"), "AveragePrice[0]", equalTo(20), "name[1]", equalTo("Seasons"),
				"AveragePrice[1]", equalTo(16),
				// check that the list of integer ids in JSON response
				// is equal to to the one of saved restaurants
				"id", equalTo(saved.stream().map(e -> e.getId().abs()).collect(Collectors.toList())));
	}

	@Test
	public void testFindByIdWithExistingRestaurant() throws Exception {
		Restaurant saved = restaurantRepository.save(new Restaurant(null, "Il Capriccio", 20));
		//restaurantRepository.flush();
		//showCurrentRestaurantsOnTheLog();
		given().when().get(url + "/api/restaurants/" + saved.getId()).then().statusCode(200).assertThat().body("id",
				equalTo(saved.getId().abs()), "name", equalTo("Il Capriccio"), "AveragePrice", equalTo(20));
	}

}
