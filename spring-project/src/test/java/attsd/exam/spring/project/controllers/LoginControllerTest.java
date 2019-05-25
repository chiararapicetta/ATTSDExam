package attsd.exam.spring.project.controllers;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.services.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {
	
	@MockBean
	private UserService userService;

	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	@Test
	public void testGetLoginPage() throws Exception {
		mvc.perform(get("/login"))
				.andExpect(view().name("login")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetSignupPage() throws Exception {
		mvc.perform(get("/signup"))
				.andExpect(view().name("signup")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetHelloPage() throws Exception {
		mvc.perform(get("/hellopage"))
				.andExpect(view().name("hellopage")).andExpect(status().isOk());
	}

	@Test
	public void testNewUserWhenUserNotExists() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("password");
		user.setUsername("username");
		mvc.perform(post("/signup")
				.param("email", user.getEmail()).param("username", user.getUsername()).param("password", user.getPassword()))
		.andExpect(view().name("login"));
	}
	
	@Test
	public void testNewUserWhenUserExists() throws Exception {
		
	}

		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
