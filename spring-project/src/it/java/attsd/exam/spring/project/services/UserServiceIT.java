package attsd.exam.spring.project.services;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.User;
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
	
	@After
	public void clear() {
		repository.deleteAll();
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
		assertEquals(u.getEmail(), user.getEmail());
		assertEquals(u.getPassword(), user.getPassword());
		assertEquals(u.getUsername(), user.getUsername());
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNotFound() {
		User user2 = new User();
		user2.setEmail("email2");
		service.loadUserByUsername(user2.getEmail());
	}
	
	@Test
	public void testLoadUserByUsernameFound() {
		User user1 = new User();
		user1.setPassword("password1");
		user1.setEmail("email1");
		repository.save(user1);
		assertThat(service.loadUserByUsername("email1"), instanceOf(User.class));
	}
	
	@Test
	public void testFindUserByEmailFound() {
		User user2 = new User();
		user2.setEmail("email2");
		user2.setPassword("pass");
		service.saveUser(user2);
		assertEquals(1, repository.count());
		User user = repository.findAll().get(0);
		assertEquals(user2.getUsername(), user.getUsername());
		assertEquals(user2.getPassword(), user.getPassword());
	
	}
	
	@Test
	public void testFindUserByEmailNotFound() {
		assertNull(repository.findByEmail("email"));
	}
}
