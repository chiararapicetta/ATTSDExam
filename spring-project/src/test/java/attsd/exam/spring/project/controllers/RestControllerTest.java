package attsd.exam.spring.project.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import attsd.exam.spring.project.services.RestaurantService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = RestaurantRestController.class)
@ContextConfiguration(classes = RestaurantRestController.class)
public class RestControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private RestaurantService service;

	@Test
	public void testStatus200() throws Exception {
		MvcResult result = mvc.perform(get("/api")).andExpect(status().isOk()).andReturn();
		assertEquals("ok", result.getResponse().getContentAsString());
	}

}
