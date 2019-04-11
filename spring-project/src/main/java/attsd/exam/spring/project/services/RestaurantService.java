package attsd.exam.spring.project.services;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.stereotype.Service;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@EnableCassandraRepositories(basePackages = { "attsd.exam.spring.project.repositories"})
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

	public Restaurant getRestaurantById(long id) {
		BigInteger toBig = BigInteger.valueOf(id);
		return restaurantRepository.findById(toBig).get();

	}

	public void storeInDb(String name, int avgPrice) {
		Restaurant r = new Restaurant();
		r.setName(name);
		r.setAveragePrice(avgPrice);
		restaurantRepository.save(r);

	}

}
