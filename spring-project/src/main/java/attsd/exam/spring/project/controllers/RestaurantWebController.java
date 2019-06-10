package attsd.exam.spring.project.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@Controller
public class RestaurantWebController {

	private RestaurantService restaurantService;
	public static final String REDIRECT = "redirect:/";
	public static final String MESSAGE = "message";
	public static final String HOMEPAGE = "index";
	public static final String UPDATE = "edit";
	public static final String ERROR = "error";
	

	@Autowired
	public RestaurantWebController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
		model.addAttribute("restaurants", allRestaurants);
		model.addAttribute(MESSAGE, allRestaurants.isEmpty() ? "No restaurant" : "");
		return HOMEPAGE;
	}

	@GetMapping("/edit/{id}")
	public String editRestaurant(@PathVariable BigInteger id, Model model) {
		Restaurant restaurantById = restaurantService.getRestaurantById(id);
		model.addAttribute("restaurant", restaurantById);
		model.addAttribute(MESSAGE, restaurantById == null ? "No restaurant found with id: " + id : "");
		return UPDATE;
	}

	@PostMapping("/save")
	public String saveRestaurant(Restaurant restaurant) {
		restaurantService.storeInDb(restaurant);
		return REDIRECT;
	}

	@GetMapping("/new")
	public String newRestaurant(Model model) {
		Restaurant restaurant = new Restaurant();
		model.addAttribute("restaurant", restaurant);
		model.addAttribute(MESSAGE, "");
		return UPDATE;
	}

	@GetMapping("/delete/{id}")
	public String deleteRestaurant(@PathVariable BigInteger id) {
		Restaurant restaurantById = restaurantService.getRestaurantById(id);
		if (restaurantById != null) {
			restaurantService.delete(restaurantById);
			return REDIRECT;
		} else {
			return ERROR;
		}
	}

	@GetMapping("/reset")
	public String resetRestaurants() {
		restaurantService.deleteAll();
		return REDIRECT;
	}

}