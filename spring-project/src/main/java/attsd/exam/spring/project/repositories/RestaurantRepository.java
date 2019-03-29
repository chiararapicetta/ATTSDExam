package attsd.exam.spring.project.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.datastax.driver.core.Session;

import attsd.exam.spring.project.model.Restaurant;

@Repository
public class RestaurantRepository {

	private Session session;

	public RestaurantRepository(Session session) {
		// TODO Auto-generated constructor stub
		this.session = session;
	}

	public List<Restaurant> retrieveAll() {
		// TODO Auto-generated method stub
		return null;
		
	}

	public Restaurant retrieveRestaurant(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
