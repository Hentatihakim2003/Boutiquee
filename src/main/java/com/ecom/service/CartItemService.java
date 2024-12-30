package com.ecom.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.ecom.model.Panier;
@Service
public class CartItemService {
	@Autowired
    private CartItemRepsitory cartItemRepsitory;
	
	public CartItem ajoutpannier(CartItem pr) {
        return cartItemRepsitory.save(pr);
    }
	/*public List<Products> getProductsInCart(Long Id) {
        List<CartItem> cartItems = cartItemRepsitory.findByCart_User_Id(Id);
        return cartItems.stream()
                .map(CartItem::getProduct)
                .collect(Collectors.toList());
    }*/

	public List<Panier> getProductsInCart(Long userId) {
	    List<CartItem> cartItems = cartItemRepsitory.findByCart_User_Id(userId);
	
	    // Conversion de chaque CartItem en Panier
	    return cartItems.stream()
	            .map(cartItem -> new Panier(
	            	cartItem.getProduct().getPrice(),
	                cartItem.getProduct().getTitle(), 
	                cartItem.getQuantity()   
	            ))
	            .collect(Collectors.toList());
	}
	
	public Double calculsomme(List<Panier> p)  {
		 Double x=0.0;
		for (int i = 0; i < p.size(); i++) {
			Panier panier = p.get(i);
	        x += panier.getPrix() * panier.getQte();	
		}
		return x;
	}
	public CartItem getCartItem(Long cartId, Long productId) {
        return cartItemRepsitory.findByCart_CartIdAndProduct_Id(cartId, productId);
    }
	public void updateCartItemQuantity(Long cartId, Long productId, int newQuantity) {
        // Rechercher le CartItem correspondant
        CartItem cartItem = cartItemRepsitory.findByCart_CartIdAndProduct_Id(cartId, productId);
        if (cartItem != null) {
        	int updatedQuantity = cartItem.getQuantity() + newQuantity;
            cartItem.setQuantity(updatedQuantity);
            cartItemRepsitory.save(cartItem);
        } else {
            throw new RuntimeException("CartItem introuvable pour cartId = " + cartId + " et productId = " + productId);
        }
    }

}
