package attsd.exam.spring.project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;
import cucumber.api.java.After;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(RestaurantService.class)
public class RestaurantServiceIT {

	@Autowired
	private RestaurantService service;

	@Autowired
	private RestaurantRepository repository;
	
	@Before
	public void setup() {
		repository.deleteAll();
	}
	
	@After
	public void clean() {
		repository.deleteAll();
	}

	@Test
	public void testServiceCanInsertIntoRepository() {
		Restaurant restaurant = service.storeInDb(new Restaurant(BigInteger.valueOf(1), "PizzeriaSpera", 10));
		assertThat(repository.findById(restaurant.getId())).isPresent();
	}
	
	@Test
	public void testServiceCanUpdateIntoRepository() {
		Restaurant restaurant = repository.save(new Restaurant(BigInteger.valueOf(1), "PizzeriaSpera", 10));
		Restaurant modifiedRestaurant = service.storeInDb(new Restaurant(restaurant.getId(), "RistoranteSpera", 15));
		assertThat(repository.findById(restaurant.getId()).get()).isEqualTo(modifiedRestaurant);
	}
	
	@Test
	public void deleteRestaurant() {
		Restaurant restaurant = service.storeInDb(new Restaurant(BigInteger.valueOf(1), "PizzeriaSpera", 10));
		assertEquals(1, repository.count());
		service.delete(restaurant);
		assertEquals(0, repository.count());
	}
	

}