package attsd.exam.spring.project.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
import org.apache.log4j.Logger;

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

	static final Logger LOGGER = Logger.getLogger(RestaurantRestControllerIT.class);

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
		given().when().get(url + "/api").then().statusCode(200).assertThat().body(is("[]"));
	}

	
}
