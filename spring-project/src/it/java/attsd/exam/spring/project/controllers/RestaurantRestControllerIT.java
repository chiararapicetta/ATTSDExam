package attsd.exam.spring.project.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;

import java.math.BigInteger;
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

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestaurantRestControllerIT {


	@LocalServerPort
	private int port;

	@Autowired
	private RestaurantRepository restaurantRepository;
	private String url;

	@Before
	public void setup() {
		url = "http://localhost:" + port;
		restaurantRepository.deleteAll();
	}

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
		List<Restaurant> saved = restaurantRepository
				.saveAll(Arrays.asList(new Restaurant(null, "Il Capriccio", 20), new Restaurant(null, "Seasons", 16)));
		given().when().get(url + "/api/restaurants").then().statusCode(200).assertThat().body("name[0]",
				equalTo("Il Capriccio"), "averagePrice[0]", equalTo(20), "name[1]", equalTo("Seasons"),
				"averagePrice[1]", equalTo(16), "id",
				equalTo(saved.stream().map(e -> e.getId().abs()).collect(Collectors.toList())));
	}

	@Test
	public void testFindByIdWithExistingRestaurant() throws Exception {
		Restaurant saved = restaurantRepository.save(new Restaurant(null, "Il Capriccio", 20));
		given().when().get(url + "/api/restaurants/" + saved.getId()).then().statusCode(200).assertThat().body("id",
				equalTo(saved.getId().abs()), "name", equalTo("Il Capriccio"), "averagePrice", equalTo(20));
	}

	@Test
	public void testFindByIdWithNonExistingRestaurant() throws Exception {
		given().when().get(url + "/api/restaurants/100").then().statusCode(200)
				.contentType("");

	}

	@Test
	public void testNewRestaurant() throws Exception {
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(new Restaurant(null, "AnticoVinaio", 6)).when()
				.post(url + "/api/restaurants/new").then().statusCode(200);
		assertThat(restaurantRepository.findAll().toString())
				.matches("\\[Restaurant \\[id=([1-9][0-9]*), name=AnticoVinaio, averagePrice=6\\]\\]");
	}

	@Test
	public void testUpdateRestaurant() throws Exception {
		Restaurant saved = restaurantRepository.save(new Restaurant(null, "AntichiSapori", 24));
		Restaurant updated = new Restaurant(null, "Saporeggiando", 30);
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put(url + "/api/restaurants/update/" + saved.getId()).then().statusCode(200);
		assertThat(restaurantRepository.findAll().toString())
				.isEqualTo("[Restaurant [id=" + saved.getId() + ", name=Saporeggiando, averagePrice=30]]");

	}

	@Test
	public void testUpdateRestaurantWithFakeId() throws Exception {
		Restaurant saved = restaurantRepository.save(new Restaurant(null, "PaneEVino", 15));
		Restaurant updated = new Restaurant(BigInteger.valueOf(1), "PizzaEBollicine", 20);
		given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updated).when()
				.put(url + "/api/restaurants/update/" + saved.getId()).then().statusCode(200);
		assertThat(restaurantRepository.findAll().toString())
				.isEqualTo("[Restaurant [id=" + saved.getId() + ", name=PizzaEBollicine, averagePrice=20]]");

	}

	@Test
	public void testDeleteRestaurant() throws Exception {
		Restaurant saved = restaurantRepository.save(new Restaurant(BigInteger.valueOf(1), "Spera", 15));
		Restaurant newsaved = restaurantRepository.save(new Restaurant(BigInteger.valueOf(2), "SperaDue", 15));
		given().when().delete(url + "/api/restaurants/delete/" + saved.getId()).then().statusCode(200);
		assertFalse(restaurantRepository.findById(BigInteger.valueOf(1)).isPresent());
		assertThat(restaurantRepository.findAll().toString())
		.isEqualTo("[Restaurant [id=" + newsaved.getId() + ", name="+ newsaved.getName()+ ", averagePrice="+ newsaved.getAveragePrice()+"]]");
	}
}
