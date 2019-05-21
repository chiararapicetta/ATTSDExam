package attsd.exam.spring.project.controllers;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import attsd.exam.spring.project.model.User;
import attsd.exam.spring.project.repositories.UserRepository;
import attsd.exam.spring.project.services.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	    
	 @GetMapping("/signup")
	 public String signup(Model model) {
		 User user = new User();
		 model.addAttribute("user", user);
		 return "signup";
	 }

	 @PostMapping("/signup")
	 public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		 User userExists = userService.findUserByEmail(user.getEmail());
		 if (userExists != null) {
				bindingResult.rejectValue("email", "error.user",
						"There is already a user registered with the username provided");
				return "error";
			}
			if (bindingResult.hasErrors()) {
				return "signup";
			} else {
				userService.saveUser(user);
				model.addAttribute("successMessage", "User has been registered successfully");
				model.addAttribute("user", new User());
				return "login";
			}
	 }
	 
	 @GetMapping("/hellopage")
	 public String helloPage(Model model) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.findUserByEmail(auth.getName());
			model.addAttribute("currentUser", user);
			model.addAttribute("fullName", "Welcome " + user.getFullname());
			model.addAttribute("adminMessage", "Content Available Only for Users with Admin Role");
			return "hellopage";
	 }

	 


}