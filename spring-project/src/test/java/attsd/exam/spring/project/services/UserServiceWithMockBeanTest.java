package attsd.exam.spring.project.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import attsd.exam.spring.project.exceptions.UserAlreadyExistsException;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;

public class UserServiceWithMockBeanTest {

    @MockBean
	private UserRepository userRepository;
    
    @Autowired
	private PasswordEncoder passEncoder;
	
	@Autowired
	private UserService userService;

	@Before
	public void init() {
		userRepository = mock(UserRepository.class);
		passEncoder = mock(PasswordEncoder.class);
		userService = new UserService(userRepository, passEncoder);
	}

	@Test
	public void testSaveUserWhenUserNotExists() {
		User user = new User();
		user.setPassword("password");
		user.setEmail("email");
		user.setUsername("fullname");
		when(userRepository.save(isA(User.class))).thenReturn(user);
		when(passEncoder.encode(anyString())).thenReturn("password");
		User u = userService.saveUser(user);
		assertEquals("email", u.getEmail());
		assertEquals("password", u.getPassword());
		assertEquals("fullname", u.getUsername());
		verify(userRepository, times(1)).save(isA(User.class));
	}

	@Test(expected = UserAlreadyExistsException.class)
	public void testSaveUserWhenUserAlreadyExists() { 
		User u = new User();
		u.setEmail("email");
		when(userRepository.findByEmail("email")).thenReturn(u);
		User user2 = new User();
		user2.setEmail("email");
		userService.saveUser(user2);
	}
	

	@Test(expected = UsernameNotFoundException.class)
	public void testLoadUserByUsernameNotFound() {
		User user2 = new User();
		user2.setEmail("email2");
		userService.loadUserByUsername(user2.getEmail());
	}
	
	@Test
	public void testLoadUserByUsernameFound() {
		User user = new User();
		user.setEmail("email");
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		assertEquals(user, userService.loadUserByUsername(user.getEmail()));
	}

	@Test
	public void testFindUserByEmail() {
		User user = new User();
		user.setEmail("email");
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		assertEquals(user, userService.findUserByEmail(user.getEmail()));
	}

	public List<User> userList() {
		List<User> list = new LinkedList<>();
		User u = new User();
		u.setEmail("email");
		u.setPassword("pass");
		list.add(u);
		return list;
	}
}
