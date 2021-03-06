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
import attsd.exam.spring.project.model.RestaurantDTO;
import attsd.exam.spring.project.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantRestController {

	private RestaurantService restaurantService;

	@Autowired
	public RestaurantRestController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GetMapping("/restaurants")
	public List<Restaurant> allRestaurants() {
		return restaurantService.getAllRestaurants();
	}

	@GetMapping("/restaurants/{id}")
	public Restaurant oneRestaurant(@PathVariable BigInteger id) {
		return restaurantService.getRestaurantById(id);
	}

	@PostMapping("/restaurants/new")
	public Restaurant newRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(restaurantDTO.getId());
		restaurant.setName(restaurantDTO.getName());
		restaurant.setAveragePrice(restaurantDTO.getAveragePrice());
		return restaurantService.storeInDb(restaurant);
	}

	@PutMapping("/restaurants/update/{id}")
	public Restaurant updateRestaurant(@PathVariable BigInteger id, @RequestBody RestaurantDTO restaurantDTO) {
		Restaurant restaurant = new Restaurant();
		restaurant.setName(restaurantDTO.getName());
		restaurant.setAveragePrice(restaurantDTO.getAveragePrice());
		restaurant.setId(id);
		return restaurantService.storeInDb(restaurant);
	}

	@DeleteMapping("/restaurants/delete/{id}")
	public void deleteRestaurant(@PathVariable BigInteger id) {
		Restaurant restaurantById = restaurantService.getRestaurantById(id);
		restaurantService.delete(restaurantById);
}
}