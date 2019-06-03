package attsd.exam.spring.project.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
	public void testNewUserWhenUserNotExists() throws Exception {
		User user = new User();
		user.setEmail("email");
		user.setPassword("pass");
		user.setUsername("user");
		mvc.perform(post("/signup")
				.param("email", user.getEmail()).param("username", user.getUsername()).param("password", user.getPassword())).andExpect(status().isOk());
;
	}
	
	@Test
	public void testNewUserWhenUserExists() throws Exception {
		User user = new User();
		user.setEmail("sara@gmail");
		user.setPassword("pass");
		user.setUsername("Sara");
		when(userService.findUserByEmail("sara@gmail")).thenReturn(user);
		User user2 = new User();
		user2.setEmail("sara@gmail");
		user.setPassword("password");
		user.setUsername("Sara10");
		mvc.perform(post("/signup")
				.param("email", user2.getEmail()).param("username", user2.getUsername()).param("password", user2.getPassword()))
		.andExpect(view().name("error"));
	}

	
	
	
	
	
	
	
	
	
	
}
