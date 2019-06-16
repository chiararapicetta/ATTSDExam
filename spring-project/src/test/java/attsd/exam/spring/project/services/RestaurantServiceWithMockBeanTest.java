package attsd.exam.spring.project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RestaurantService.class)
public class RestaurantServiceWithMockBeanTest {

	@MockBean
	private RestaurantRepository restaurantRepository;

	@Autowired
	private RestaurantService restaurantService;

	@Test
	public void testGetAllRestaurantswithNoRestaurants() {
		assertEquals(0, restaurantService.getAllRestaurants().size());
		verify(restaurantRepository, times(1)).findAll();
	}

	@Test
	public void testGetAllRestaurantswithOneRestaurants() {
		Restaurant r= new Restaurant(BigInteger.valueOf(1), "first", 10);
		when(restaurantRepository.findAll())
				.thenReturn(Arrays.asList(r));
		assertThat(restaurantService.getAllRestaurants()).containsExactly(r);
		verify(restaurantRepository, times(1)).findAll();
	}

	@Test
	public void testGetAllRestaurantswithRestaurants() {
		Restaurant restaurant1 = new Restaurant(BigInteger.valueOf(1), "first", 20);
		Restaurant restaurant2 = new Restaurant(BigInteger.valueOf(2), "second", 28);
		when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));
		assertThat(restaurantService.getAllRestaurants()).containsExactly(restaurant1, restaurant2);
		verify(restaurantRepository, times(1)).findAll();
	}

	@Test
	public void testGetRestaurantByIdFound() {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "first", 20);
		Optional<Restaurant> expected = Optional.of(restaurant);
		when(restaurantRepository.findById(BigInteger.ONE)).thenReturn(expected);
		assertThat(restaurantService.getRestaurantById(BigInteger.ONE)).isSameAs(restaurant);
		verify(restaurantRepository, times(1)).findById(BigInteger.ONE);
	}

	@Test
	public void testGetRestaurantByIdNotFound() {
		Restaurant actual = restaurantService.getRestaurantById(BigInteger.ONE);
		assertNull(actual);
		verify(restaurantRepository, times(1)).findById(BigInteger.ONE);
	}

	@Test
	public void testSave() {
		ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
		Restaurant r = new Restaurant(BigInteger.ONE, "Pizzeria", 10);
		restaurantService.storeInDb(r);
		verify(restaurantRepository, times(1)).save(captor.capture());
		Restaurant passedToRepository = captor.getValue();
		assertEquals(BigInteger.ONE, passedToRepository.getId());
		assertEquals("Pizzeria", passedToRepository.getName());
		assertEquals(10, passedToRepository.getAveragePrice());
	}

	@Test
	public void testDeleteRestaurant() {
		Restaurant r = new Restaurant(BigInteger.ONE, "Pizzeria", 10);
		restaurantService.delete(r);
		verify(restaurantRepository, times(1)).delete(r);
	}

	@Test
	public void testDeleteAllRestaurants() {
		restaurantService.deleteAll();
		verify(restaurantRepository, times(1)).deleteAll();
	}
}
