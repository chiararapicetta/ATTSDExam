package attsd.exam.spring.project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=RestaurantService.class)
public class RestaurantServiceWithMockBeanTest {
	
	@MockBean
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Test
	public void testGetMaxAveregePriceRestaurantwithNoRestaurants() {
		assertNull(restaurantService.getMaxAveragePriceRestaurant());
		verify(restaurantRepository).retrieveAll();
	} 
	
	@Test
	public void testGetAllRestaurantswithRestaurants() {
		Restaurant restaurant1 = new Restaurant(1, "first", 20);
		Restaurant restaurant2 = new Restaurant(2, "second", 28);
		when(restaurantRepository.retrieveAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));
		assertThat(restaurantService.getAllRestaurants()).containsExactly(restaurant1, restaurant2);
		verify(restaurantRepository).retrieveAll();
	}

}
