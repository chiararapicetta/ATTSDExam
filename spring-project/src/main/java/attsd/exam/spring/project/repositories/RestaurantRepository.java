package attsd.exam.spring.project.repositories;


import java.math.BigInteger;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;


import attsd.exam.spring.project.model.Restaurant;

@Repository
public interface RestaurantRepository extends CassandraRepository<Restaurant, BigInteger>{

}
