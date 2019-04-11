package attsd.exam.spring.project;

import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import attsd.exam.spring.project.config.CassandraConfig;
import attsd.exam.spring.project.repositories.RestaurantRepository;
import attsd.exam.spring.project.services.RestaurantService;

@Import(CassandraConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITProva {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private RestaurantService service;
	
	@Test
	public void prova() {
		service.storeInDb("ciao", 20);
		new Scanner(System.in).nextInt();
	}

}
