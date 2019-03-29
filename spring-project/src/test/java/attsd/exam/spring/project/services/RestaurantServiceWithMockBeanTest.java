package attsd.exam.spring.project.services;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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

}
