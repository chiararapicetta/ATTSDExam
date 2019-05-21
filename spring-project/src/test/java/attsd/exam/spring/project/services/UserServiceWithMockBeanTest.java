package attsd.exam.spring.project.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
	public void testSaveUser() {
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
/*
	@Test(expected = RuntimeException.class)
	public void testUsernameAlreadyExists() { 
		when(userRepository.findByEmail(anyString()).thenReturn(null));
	}
	*/

	@Test(expected = UsernameNotFoundException.class)
	public void testUsernameNotFound() {
		User user2 = new User();
		user2.setEmail("email2");
		userService.loadUserByUsername(user2.getEmail());
	}

	@Test
	public void testFindUserByEmail() {
		User user = new User();
		user.setEmail("email");
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		when(userService.findUserByEmail("email")).thenReturn(user);
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
