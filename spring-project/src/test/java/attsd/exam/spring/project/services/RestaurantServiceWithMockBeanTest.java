package attsd.exam.spring.project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
	public void testGetMaxAveregePriceRestaurantwithNoRestaurants() {
		assertNull(restaurantService.getMaxAveragePriceRestaurant());
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetMaxAveragePriceRestaurantwithOneRestaurant() {
		Restaurant r;
		when(restaurantRepository.findAll()).thenReturn(Arrays.asList(r = new Restaurant(BigInteger.valueOf(1), "test", 20)));
		assertEquals(r, restaurantService.getMaxAveragePriceRestaurant());
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetMaxAveragePriceRestaurant() {
		Restaurant r;
		when(restaurantRepository.findAll())
				.thenReturn(Arrays.asList(new Restaurant(BigInteger.valueOf(1), "test1", 20), r = new Restaurant(BigInteger.valueOf(2), "test", 30)));
		assertEquals(r, restaurantService.getMaxAveragePriceRestaurant());
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetAllRestaurantswithNoRestaurants() {
		assertEquals(0, restaurantService.getAllRestaurants().size());
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetAllRestaurantswithOneRestaurants() {
		Restaurant restaurant1;
		when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1 = new Restaurant(BigInteger.valueOf(1), "first", 10)));
		assertThat(restaurantService.getAllRestaurants()).containsExactly(restaurant1);
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetAllRestaurantswithRestaurants() {
		Restaurant restaurant1 = new Restaurant(BigInteger.valueOf(1), "first", 20);
		Restaurant restaurant2 = new Restaurant(BigInteger.valueOf(2), "second", 28);
		when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));
		assertThat(restaurantService.getAllRestaurants()).containsExactly(restaurant1, restaurant2);
		verify(restaurantRepository, Mockito.times(1)).findAll();
	}

	@Test
	public void testGetRestaurantByIdFound() {
		Restaurant restaurant = new Restaurant(BigInteger.valueOf(1), "first", 20);
		Optional<Restaurant> expected = Optional.of(restaurant);
		when(restaurantRepository.findById(BigInteger.ONE)).thenReturn(expected);
		assertThat(restaurantService.getRestaurantById(1)).isSameAs(restaurant);
		verify(restaurantRepository, Mockito.times(1)).findById(BigInteger.ONE);
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetRestaurantByIdNotFound() {
		Optional<Restaurant> actual = Optional.of(restaurantService.getRestaurantById(0));
		assertNotNull(actual);
		actual.get();
		verify(restaurantRepository, Mockito.times(1)).findById(BigInteger.ZERO);
	}

	@Test
	public void testSave() {
		ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
		restaurantService.storeInDb("test", 10);
		verify(restaurantRepository, Mockito.times(1)).save(captor.capture());
		Restaurant passedToRepository = captor.getValue();
		assertEquals("test", passedToRepository.getName());
		assertEquals(10, passedToRepository.getAveragePrice());
	}

}
