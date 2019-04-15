package attsd.exam.spring.project.controllers;


import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RestController
public class RestaurantRestController {
	
	@Autowired
	private RestaurantService restaurantService;

	@GetMapping("/api/restaurants")
	public List<Restaurant> allRestaurants() {
		return restaurantService.getAllRestaurants();
	}


	@GetMapping("/api/restaurants/{id}")
	public Restaurant oneRestaurant(@PathVariable BigInteger id) {
	return restaurantService.getRestaurantById(id);
	}
}