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
import com.ecom.repsitory.ProductRepsitory;
import com.ecom.repsitory.CartItemRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
import com.ecom.model.Cart;
import com.ecom.model.CartItem;
import com.ecom.model.Products;
@Service
public class CartService {
	@Autowired
    private CartRepsitory cartRepository;
	@Autowired
    private CartItemRepsitory cartItemRepsitory;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        //if (cart != null) {
        	cartItemRepsitory.deleteByCart(cart);
        	cartRepository.delete(cart);
        //}
    }
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
