package attsd.exam.spring.project.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.RestaurantRepository;
import attsd.exam.spring.project.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(UserService.class)
public class UserServiceIT {

	@Autowired
	private UserService service;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Before
	public void setup() {
		repository.deleteAll();
		service = new UserService(repository, encoder);
	}
	
	@Test
	public void testSaveUserWhenUserNotExists() {
		User user = new User();
		user.setPassword("password");
		user.setEmail("email");
		user.setUsername("username");
		service.saveUser(user);
		User u = repository.findAll().get(0);
		assertEquals(1, repository.count());
		assertEquals(u.getEmail(), u.getEmail());
		assertEquals(u.getPassword(), u.getPassword());
		assertEquals(u.getUsername(), u.getUsername());
	}
}
