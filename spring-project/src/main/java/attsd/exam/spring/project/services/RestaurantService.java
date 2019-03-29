package attsd.exam.spring.project.services;

import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@Service
public class RestaurantService {
	
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	public RestaurantService(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	public Restaurant getMaxAveragePriceRestaurant() {
		List<Restaurant> restaurants = restaurantRepository.retrieveAll();
		return restaurants.stream().max(Comparator.comparing(Restaurant::getAveragePrice)).orElse(null);
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.retrieveAll();
	}

	public Restaurant getRestaurantById(long id) {
		return restaurantRepository.retrieveRestaurant(id);
	}

}
