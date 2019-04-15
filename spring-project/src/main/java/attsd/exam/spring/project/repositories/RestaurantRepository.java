package attsd.exam.spring.project.repositories;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import attsd.exam.spring.project.model.Restaurant;

public interface RestaurantRepository {

	List<Restaurant> findAll();

	Optional<Restaurant> findById(BigInteger toBig);

	void save(Restaurant r);

}
