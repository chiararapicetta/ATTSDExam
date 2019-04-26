package attsd.exam.spring.project.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@RestController
public class RestaurantRestController {

	
	@Autowired
	private RestaurantService restaurantService;


	@GetMapping("/api")
	public List<Restaurant> allRestaurants() {
		return restaurantService.getAllRestaurants();
	}

	@GetMapping("api/restaurants/{id}")
	public Restaurant oneRestaurant(@PathVariable BigInteger id) {
		return restaurantService.getRestaurantById(id);
	}

	@PostMapping("api/restaurants/new")
	public Restaurant newRestaurant(@RequestBody Restaurant restaurant) {
		return restaurantService.storeInDb(restaurant);
	}

	@PutMapping("api/restaurants/update/{id}")
	public Restaurant updateRestaurant(@PathVariable BigInteger id, @RequestBody Restaurant restaurant) {
		restaurant.setId(id);
		return restaurantService.storeInDb(restaurant);
	}

	@DeleteMapping("api/restaurants/delete/{id}")
	public void deleteRestaurant(@PathVariable BigInteger id) {
		restaurantService.delete(id);
	}

}