package attsd.exam.spring.project.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.Session;

import attsd.exam.spring.project.model.Restaurant;

@Repository
public class RestaurantRepository {

	private static final String TABLE_NAME = "RESTAURANTS";
	
	private Session session;

	@Autowired
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
	
	public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(").append("id uuid PRIMARY KEY, ").append("Name text,");
        final String query = sb.toString();
        session.execute(query);
    }

}
