package attsd.exam.spring.project.services;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
		List<Restaurant> restaurants = getAllRestaurants();
		return restaurants.stream().max(Comparator.comparing(Restaurant::getAveragePrice)).orElse(null);
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	public Restaurant getRestaurantById(BigInteger id) {
		Optional<Restaurant> result = restaurantRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

}
