package attsd.exam.spring.project.controllers;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestaurantWebController.class)
public class RestaurantWebControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private RestaurantService restaurantService;

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	public void testEmptyRestaurantList() throws Exception {
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("restaurants", new ArrayList<Restaurant>()))
				.andExpect(model().attribute("message", "No restaurant"));
		verify(restaurantService).getAllRestaurants();
	}

	@Test
	public void testNotEmptyRestaurantList() throws Exception {
		List<Restaurant> restaurants = Arrays.asList(new Restaurant(BigInteger.valueOf(1), "DaAndrea", 30));
		when(restaurantService.getAllRestaurants()).thenReturn(restaurants);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("restaurants", restaurants))
				.andExpect(model().attribute("message", ""));
		verify(restaurantService).getAllRestaurants();
	}

	@Test
	public void testSingleRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "Zorba", 23);
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1))).thenReturn(restaurant);
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", restaurant)).andExpect(model().attribute("message", ""));
		verify(restaurantService).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	public void testSingleRestaurantNotFound() throws Exception {
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", nullValue()))
				.andExpect(model().attribute("message", "No restaurant found with id: 1"));
		verify(restaurantService).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	public void testPostRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(0), "LaFiaccola", 45);
		mvc.perform(post("/save")
				.param("id", "" + restaurant.getId())
				.param("name", restaurant.getName())
				.param("averagePrice","" + restaurant.getAveragePrice()))
		.andExpect(view().name("redirect:/"));
		verify(restaurantService).storeInDb(restaurant);
	}

	@Test
	public void testNewRestaurant() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", new Restaurant(BigInteger.valueOf(1), "nuovoRistorante", 0)))
				.andExpect(model().attribute("message", ""));
		verifyZeroInteractions(restaurantService);
	}

}
