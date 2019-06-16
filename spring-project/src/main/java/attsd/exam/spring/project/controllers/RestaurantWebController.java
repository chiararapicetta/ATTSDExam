package attsd.exam.spring.project.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import attsd.exam.spring.project.model.Restaurant;
import attsd.exam.spring.project.services.RestaurantService;

@Controller
public class RestaurantWebController {

	private RestaurantService restaurantService;
	public static final String REDIRECT = "redirect:/";
	public static final String MESSAGE = "message";
	public static final String HOMEPAGE = "index";
	public static final String EDIT = "edit";
	public static final String ERROR = "error";

	@Autowired
	public RestaurantWebController(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();
		model.addAttribute("restaurants", allRestaurants);
		model.addAttribute(MESSAGE, allRestaurants.isEmpty() ? "No restaurants" : "");
		return HOMEPAGE;
	}

	@GetMapping(value = "/edit")
	public String editRestaurant(@RequestParam(name = "id") BigInteger id, Model model) {
		Restaurant restaurantById = restaurantService.getRestaurantById(id);
		if (restaurantById != null) {
			model.addAttribute("restaurant", restaurantById);
			model.addAttribute(MESSAGE, "Edit restaurant");
			return EDIT;
		} else {
			model.addAttribute(MESSAGE, "Error");
			return ERROR;
		}
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
		return EDIT;
	}

	@GetMapping(value = "/delete")
	public String deleteRestaurant(@RequestParam(name = "id") BigInteger id, Model model) {
		Restaurant restaurantById = restaurantService.getRestaurantById(id);
		if (restaurantById != null) {
			restaurantService.delete(restaurantById);
			return REDIRECT;
		} else {
			model.addAttribute(MESSAGE, "Error");
			return ERROR;
		}
	}

	@GetMapping("/reset")
	public String resetRestaurants() {
		restaurantService.deleteAll();
		return REDIRECT;
	}

}