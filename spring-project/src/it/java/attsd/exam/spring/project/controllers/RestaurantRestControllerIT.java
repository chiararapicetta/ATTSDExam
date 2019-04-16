package attsd.exam.spring.project.controllers;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)

public class RestaurantRestControllerIT {

	@LocalServerPort
	private int port;
}
