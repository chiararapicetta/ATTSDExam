package attsd.exam.spring.project.controllers;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import attsd.exam.spring.project.config.WebSecurityConfig;
import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@Import({WebSecurityConfig.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestaurantWebControllerIT {

	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private RestaurantRepository repository;

	private MockMvc mvc;
	
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		repository.deleteAll();
	}
	
	@After
	public void clearAll() {
		repository.deleteAll();
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
				.andExpect(model().attribute("message", "No restaurants"));
		assertEquals(0, repository.count());
	}


	@Test
	@WithMockUser
	public void testTwoRestaurants() throws Exception {
		List<Restaurant> restaurants = Arrays.asList(new Restaurant(null, "Il Capriccio", 20), new Restaurant(null, "Seasons", 16));
		repository.saveAll(restaurants);
		mvc.perform(get("/")).andExpect(view().name("index")).andExpect(model().attribute("restaurants", restaurants))
				.andExpect(model().attribute("message", ""));
		assertEquals(2, repository.count());
	}

	@Test
	@WithMockUser
	public void testSingleRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "Zorba", 23);
		repository.save(restaurant);
		String id ="1";
		mvc.perform(get("/edit?id="+id)).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", restaurant)).andExpect(model().attribute("message", ""));
		assertEquals(1, repository.count());
	}

	@Test
	@WithMockUser
	public void testSingleRestaurantNotFound() throws Exception {
		String id = "1";
		mvc.perform(get("/edit?id="+id)).andExpect(view().name("error"))
				.andExpect(model().attribute("restaurant", nullValue()));
		assertEquals(0, repository.count());
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
		assertEquals(1, repository.count());
	}

	
	@Test
	@WithMockUser
	public void testNewRestaurant() throws Exception {
		mvc.perform(get("/new")).andExpect(view().name("edit"))
				.andExpect(model().attribute("restaurant", new Restaurant(null, null, 0)))
				.andExpect(model().attribute("message", ""));
	}

	@Test
	@WithMockUser
	public void testDeleteRestaurant() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "LaFiaccola", 45);
		repository.save(restaurant);
		Restaurant r = new Restaurant(BigInteger.valueOf(2), "CacioEPepe", 28);
		repository.save(r);
		String id = "1";
		mvc.perform(get("/delete?id="+id)).andExpect(view().name("redirect:/"));
		assertEquals(1, repository.count());
	}
	
	@Test
	@WithMockUser
	public void testResetRestaurants() throws Exception {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "LaFiaccola", 45);
		repository.save(restaurant);
		Restaurant restaurant2 = new Restaurant(BigInteger.valueOf(2), "CacioEPepe", 28);
		repository.save(restaurant2);
		assertEquals(2, repository.count());
		mvc.perform(get("/reset")).andExpect(view().name("redirect:/"));
		assertEquals(0, repository.count());
	}
}

