package com.ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.ecom.model.Users;
import com.ecom.repsitory.UserRepsitory;

import jakarta.annotation.PostConstruct;

import com.ecom.model.Adresse;
import com.ecom.repsitory.AdresseRepsitory;
import com.ecom.repsitory.CartRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
import com.ecom.model.Cart;
@Service
public class UserService {

    @Autowired
    private UserRepsitory userRepsitory;

    @Autowired
    private AdresseRepsitory adresseRepsitory;
    
    @Autowired
    private CartRepsitory cartRepsitory;

    public Users createUser(Users user) {
    	 Users savedUser = userRepsitory.save(user);
    	 Cart cart = new Cart();
         cart.setUser(savedUser);
         cartRepsitory.save(cart);
         savedUser.setCart(cart);
         return savedUser;
    }
    public Optional<Users> login(String email, String password) {
        return userRepsitory.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }
    public Optional<Users> findByEmail(String email) {
        return userRepsitory.findByEmail(email);
    }

    public Adresse addAdresseToUser(Long userId, Adresse adresse) {
        Optional<Users> userOptional = userRepsitory.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            adresse.setUser(user); // Lier l'adresse à l'utilisateur
            return adresseRepsitory.save(adresse);
        }
        throw new RuntimeException("User not found");
    }

    public List<Adresse> getAdressesForUser(Long userId) {
        Optional<Users> userOptional = userRepsitory.findById(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getAdresses();
        }
        throw new RuntimeException("User not found");
    }
    @PostConstruct
    public void addCartsForExistingUsers() {
    List<Users> users = userRepsitory.findAll();
    for (Users user : users) {
        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setUser(user); // Link the cart to the user
            cartRepsitory.save(cart); // Save the cart
            user.setCart(cart); // Associate the cart with the user
            userRepsitory.save(user); // Update the user with the new cart
        }
    }
}
}
