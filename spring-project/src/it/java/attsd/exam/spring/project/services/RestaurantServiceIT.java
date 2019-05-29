package attsd.exam.spring.project.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.repositories.RestaurantRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(RestaurantService.class)
public class RestaurantServiceIT {

	@Autowired
	private RestaurantService service;

	@Autowired
	private RestaurantRepository repository;

	@Test
	public void testServiceCanInsertIntoRepository() {
		Restaurant r = service.storeInDb(new Restaurant(BigInteger.valueOf(1), "PizzeriaSpera", 10));
		assertThat(repository.findById(r.getId())).isPresent();
	}

}