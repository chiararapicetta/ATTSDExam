package attsd.exam.spring.project.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import attsd.exam.spring.project.config.WebSecurityConfig;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;

@RunWith(SpringRunner.class)
@Import({ WebSecurityConfig.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginControllerIT {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserRepository repository;

	private MockMvc mvc;

	private MultiValueMap<String, String> params = new HttpHeaders();

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
		repository.deleteAll();
	}

	@After
	public void clearAll() {
		repository.deleteAll();
		params.clear();
	}

	@Test
	public void testGetLogin() throws Exception {
		mvc.perform(get("/login")).andExpect(view().name("login")).andExpect(status().isOk());
	}

	@Test
	public void testStatus200() throws Exception {
		mvc.perform(get("/login")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testReturnLogin() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/login")).andReturn().getModelAndView(), "login");
	}

	@Test
	public void testGetSignup() throws Exception {
		mvc.perform(get("/signup")).andExpect(view().name("signup")).andExpect(status().isOk());
	}

	@Test
	public void testSaveUser() throws Exception {
		mvc.perform(post("/signup").param("email", "email").param("username", "username").param("password", "pass")
				.with(csrf())).andExpect(status().is2xxSuccessful()).andExpect(view().name("login"));
		assertEquals(1, repository.count());

	}

	@Test
	public void testSaveUserAlreadyExists() throws Exception {
		User user = new User();
		user.setEmail("email1");
		user.setUsername("usern");
		user.setPassword("pass");
		repository.save(user);
		assertEquals(1, repository.count());
		mvc.perform(post("/signup").param("email", "email1").param("username", "usern").param("password", "pass")
				.with(csrf())).andExpect(status().is2xxSuccessful()).andExpect(view().name("error"));
		assertEquals(1, repository.count());

	}
}
