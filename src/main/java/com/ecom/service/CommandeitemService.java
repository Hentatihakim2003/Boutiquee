package com.ecom.service;
import com.ecom.model.Adresse;
import com.ecom.model.Cart;
import com.ecom.model.CartItem;
import com.ecom.model.Commande;
import com.ecom.model.Commandeitem;
import com.ecom.repsitory.CommandeRepsitory;
import com.ecom.repsitory.CommandeitemRepsitory;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CommandeitemService {

    @Autowired
    private CommandeitemRepsitory commandeitemRepsitory;
	@Autowired
    private UserService userService;
	@Autowired
    private ProductService productService;
	@Autowired
    private CartItemService cartItemService;
	@Autowired
    private CartService cartService;
	@Autowired
    private CommandeRepsitory commandeRepsitory;

    // Ajouter un nouvel OrderItem
    public Commandeitem addOrderItem(Commandeitem orderItem) {
        return commandeitemRepsitory.save(orderItem);
    }

    // Trouver tous les OrderItems par Order ID
    public List<Commandeitem> getOrderItemsByOrderId(Long orderId) {
        return commandeitemRepsitory.findByCommandeOrderId(orderId);
    }

    // Supprimer un OrderItem par son ID
    public void deleteOrderItem(Long orderItemId) {
    	commandeitemRepsitory.deleteById(orderItemId);
    }
    @Transactional
    public Commande placeOrder(Long userId, Long addressId) {
        Cart cart = cartService.getCartByUserId(userId);
        Adresse adr = productService.adressebyid(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Adresse non trouvée pour l'ID : " + addressId));
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Le panier est vide !");
        }

        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Commande order = new Commande();
        order.setUser(cart.getUser());
        order.setAddress(adr);
        order.setTotalPrice(totalPrice);
        order.setStatus(Commande.Status.PENDING); 

        order = commandeRepsitory.save(order);

        // 4. Transformer les CartItems en OrderItems
        for (CartItem cartItem : cartItems) {
        	Commandeitem orderItem = new Commandeitem();
            orderItem.setCommande(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());

            // Sauvegarder l'OrderItem
            commandeitemRepsitory.save(orderItem);
        }

        cart.getCartItems().clear();  // Supprime les relations en mémoire
        cartService.saveCart(cart); 

        return order;
    }
}
