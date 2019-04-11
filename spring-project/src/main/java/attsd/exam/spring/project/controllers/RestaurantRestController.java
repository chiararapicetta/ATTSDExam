package attsd.exam.spring.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import attsd.exam.spring.project.services.RestaurantService;

@RestController
public class RestaurantRestController {

	@Autowired
	private RestaurantService service;

	@GetMapping("/api")
	public String index() {
		return "ok";

	}

	@GetMapping("/api/all")
	public String getAll() {
		return service.getAllRestaurants().toString();
	}

}
