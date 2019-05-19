package attsd.exam.spring.project.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;

public class UserServiceWithMockBeanTest {

	private UserRepository userRepository;
	private PasswordEncoder passEncoder;
	private UserService userService;

	@Before
	public void init() {
		userRepository = mock(UserRepository.class);
		passEncoder = mock(PasswordEncoder.class);
		userService = new UserService(userRepository, passEncoder);
	}

	@Test
	public void testValidRegistration() {
		User user = new User();
		user.setPassword("password");
		user.setEmail("email");
		user.setFullname("fullname");
		when(userRepository.save(isA(User.class))).thenReturn(user);
		when(passEncoder.encode(anyString())).thenReturn("password");
		User u = userService.saveUser(user);
		assertEquals("email", u.getEmail());
		assertEquals("password", u.getPassword());
		assertEquals("fullname", u.getFullname());
		verify(userRepository, times(1)).save(isA(User.class));
	}
}