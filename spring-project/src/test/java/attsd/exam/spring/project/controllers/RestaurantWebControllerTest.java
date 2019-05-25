package attsd.exam.spring.project.controllers;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestaurantWebController.class)
public class RestaurantWebControllerTest {

	private MockMvc mvc;
	//private MultiValueMap<String, String> params;
	
	@Autowired
	private WebApplicationContext context;

	@MockBean
	private RestaurantService restaurantService;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		//params = new HttpHeaders();
	}
	
	@After
	public void clearAll() {
		//params.clear();
}
	
	@Test
	@WithMockUser
	public void testGetIndex() throws Exception {
		mvc.perform(get("/"))
				.andExpect(view().name("index")).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testStatus200() throws Exception {
		mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
	}

	@Test
	@WithMockUser
	public void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/")).andReturn().getModelAndView(), "index");
	}

	@Test
	@WithMockUser
	public void testEmptyRestaurantList() throws Exception {
		mvc.perform(get("/")).andExpect(view().name("index"))
				.andExpect(model().attribute("restaurants", new ArrayList<Restaurant>()))
				.andExpect(model().attribute("message", "No restaurant"));
		verify(restaurantService).getAllRestaurants();
	}

	@Test
	@WithMockUser
	public void testNotEmptyRestaurantList() throws Exception {
		List<Restaurant> restaurants = Arrays.asList(new Restaurant(BigInteger.valueOf(1), "DaAndrea", 30));
		when(restaurantService.getAllRestaurants()).thenReturn(restaurants);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("restaurants", restaurants))
				.andExpect(model().attribute("message", ""));
		verify(restaurantService).getAllRestaurants();
	}

	@Test
	@WithMockUser
	public void testSingleRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "Zorba", 23);
		when(restaurantService.getRestaurantById(BigInteger.valueOf(1))).thenReturn(restaurant);
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", restaurant)).andExpect(model().attribute("message", ""));
		verify(restaurantService).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	@WithMockUser
	public void testSingleRestaurantNotFound() throws Exception {
		mvc.perform(get("/edit/1")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", nullValue()))
				.andExpect(model().attribute("message", "No restaurant found with id: 1"));
		verify(restaurantService).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	@WithMockUser
	public void testPostRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "LaFiaccola", 45);
		mvc.perform(post("/save")
				.param("id", "" + restaurant.getId())
				.param("name", restaurant.getName())
				.param("averagePrice","" + restaurant.getAveragePrice()))
		.andExpect(view().name("redirect:/"));
		verify(restaurantService).storeInDb(restaurant);
	}

	
	@Test
	@WithMockUser
	public void testNewRestaurant() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", new Restaurant(BigInteger.valueOf(1), null, 0)))
				.andExpect(model().attribute("message", ""));
		verify(restaurantService).getRestaurantById(BigInteger.valueOf(1));
	}

	@Test
	@WithMockUser
	public void testDeleteRestaurant() throws Exception {
		mvc.perform(get("/delete/1")).andExpect(view().name("redirect:/"));
		verify(restaurantService).delete(BigInteger.valueOf(1));
	}
	
	@Test
	@WithMockUser
	public void testResetRestaurants() throws Exception {
		mvc.perform(get("/reset")).andExpect(view().name("redirect:/"));
		verify(restaurantService).deleteAll();
	}
}
