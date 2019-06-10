package attsd.exam.spring.project.repositories;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import attsd.exam.spring.project.model.Restaurant;

public interface RestaurantRepository extends MongoRepository<Restaurant, BigInteger> {

	
}


