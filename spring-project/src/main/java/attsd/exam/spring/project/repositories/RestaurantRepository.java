package attsd.exam.spring.project.repositories;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import attsd.exam.spring.project.model.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, BigInteger> {


	@Query("Select r from Restaurant r where r.averagePrice = :n")
	List<Restaurant> findAllRestaurantsWithTheSameAveragePrice(@Param("n") int n);
	

	@Query("Select r from Restaurant r order by name")
	List<Restaurant> findAllRestaurantsOrderByName();

	
}

