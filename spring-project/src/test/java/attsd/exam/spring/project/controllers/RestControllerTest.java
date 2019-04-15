package attsd.exam.spring.project.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestaurantRestController.class)
public class RestControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private RestaurantService restaurantService;

	@Test
	public void testAllEmployees() throws Exception {
		when(restaurantService.getAllRestaurants())
				.thenReturn(Arrays.asList(new Restaurant(BigInteger.valueOf(1), "LaVillaDeiLimoni", 25),
						new Restaurant(BigInteger.valueOf(2), "Oleandro", 45)));
		this.mvc.perform(get("/api/restaurants").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is("LaVillaDeiLimoni")))
				.andExpect(jsonPath("$[0].averagePrice", is(25))).andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Oleandro"))).andExpect(jsonPath("$[1].averagePrice", is(45)));
		verify(restaurantService, times(1)).getAllRestaurants();
	}

	@Test
	public void testAllRestaurantsEmpty() throws Exception {
		this.mvc.perform(get("/api/restaurants").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
		// the above checks that the content is an empty JSON list
		verify(restaurantService, times(1)).getAllRestaurants();
	}

	@Test
	public void testFindByIdWithExistingRestaurant() throws Exception {
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1))).thenReturn(new Restaurant(BigInteger.valueOf(1), "Africhella", 50));
		this.mvc.perform(get("/api/restaurants/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Africhella")))
				.andExpect(jsonPath("$.averagePrice", is(50)));
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	public void testFindByIdWithNotFoundRestaurant() throws Exception {
		this.mvc.perform(get("/api/restaurants/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
		verify(restaurantService, times(1)).getRestaurantById(BigInteger.valueOf(1));
	}
}
