package attsd.exam.spring.project.services;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;


@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;
	
	public Restaurant getMaxAveragePriceRestaurant() {
		List<Restaurant> restaurants = restaurantRepository.findAll();
		return restaurants.stream().max(Comparator.comparing(Restaurant::getAveragePrice)).orElse(null);
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	public Restaurant getRestaurantById(BigInteger id) {
		return restaurantRepository.findById(id).orElse(null);
	}

	public Restaurant storeInDb(Restaurant r) {	
		return restaurantRepository.save(r);
	}

	public void delete(Restaurant restaurant) {
			restaurantRepository.delete(restaurant);
	}

	public void deleteAll() {
		restaurantRepository.deleteAll();
	}
}