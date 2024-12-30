package com.ecom.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.ecom.model.Users;
import com.ecom.repsitory.UserRepsitory;


import com.ecom.model.Adresse;
import com.ecom.repsitory.AdresseRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
import com.ecom.service.CommandeitemService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/Auth")
public class AccountController {
	@Autowired
    private UserService userService;
	@Autowired
    private CommandeitemService commandeitemService;
	
	@GetMapping("/login")
	public String login() {
		return "Login";
		}
    @PostMapping("/register")
    public String registerUser(@ModelAttribute Users user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Email déjà utilisé");
            return "register"; // Si l'email existe déjà
        }

        userService.createUser(user);  // Enregistrer l'utilisateur
        return "redirect:/Auth/login"; // Rediriger vers la page de connexion après inscription réussie
    }
    
    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model, HttpSession session)  {
        
        if ("admin".equals(email) && "admin".equals(password)) {
        	session.setAttribute("role", "admin");
        	
            return "redirect:/Admin"; // Redirige vers la page admin
        } 
        Optional<Users> userOptional = userService.login(email, password);
        if (userOptional.isPresent()) {
            // Si la connexion réussit
        	Users user = userOptional.get();
        	session.setAttribute("role", "user");
        	session.setAttribute("loggedUser", user);
            return "redirect:/"; // Redirige vers la page d'accueil
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect");
            return "login";
        }
    
    }
    	
    	

		@GetMapping("/Signup")
		public String register() {
			return "Signup";
		}
		
		@GetMapping("/logout")
		public String logout(HttpSession session) {
			session.invalidate();
			return "redirect:/"; 
		}
		@PostMapping("/Adress")
		public String AdresseAdresse(@ModelAttribute Adresse adr, Model model,HttpSession session) {
	        Users user = (Users) session.getAttribute("loggedUser");
	        userService.addAdresseToUser(user.getId(), adr);
	        return "redirect:/ModeExpedition";
		}
		@PostMapping("/submit-address")
		public String choixadr(@RequestParam Long selectedAddress,Model model,HttpSession session) {
	        //Users user = (Users) session.getAttribute("loggedUser");
	        //userService.addAdresseToUser(user.getId(), adr);
			Users user = (Users) session.getAttribute("loggedUser");
			commandeitemService.placeOrder( user.getId(),selectedAddress);
	        return "redirect:/";
		}
		
}

