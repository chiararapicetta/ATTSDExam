package attsd.exam.spring.project.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.Restaurant;

@DataJpaTest
@RunWith(SpringRunner.class)
public class RestaurantRepositoryTest {

	@Autowired
	private RestaurantRepository repository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void findAllTheRestaurantsWithSameAveragePrice() {
		Restaurant rest1 = entityManager.persistFlushFind(new Restaurant(null, "Borgo al Cotone", 20));
		entityManager.persistFlushFind(new Restaurant(null, "Scaraboci", 25));
		Restaurant rest3 = entityManager.persistFlushFind(new Restaurant(null, "Tiramisu", 20));
		List<Restaurant> list = repository.findAllRestaurantsWithTheSameAveragePrice(20);
		assertThat(list).containsExactly(rest1, rest3);
	}

	@Test
	public void findAllTheRestaurantsOrderByName() {
		Restaurant r1 = entityManager.persistFlushFind(new Restaurant(null, "Zibibbo", 10));
		Restaurant r2 = entityManager.persistFlushFind(new Restaurant(null, "Brindisi", 20));
		Restaurant r3 = entityManager.persistFlushFind(new Restaurant(null, "MercatoCentrale", 20));
		Restaurant r4 = entityManager.persistFlushFind(new Restaurant(null, "Amalfi", 20));
		List<Restaurant> list = repository.findAllRestaurantsOrderByName();
		assertThat(list).containsExactly(r4, r2, r3, r1);
	}

}
