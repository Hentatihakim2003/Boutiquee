package com.ecom.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.ecom.model.Users;
import com.ecom.repsitory.UserRepsitory;


import com.ecom.model.Adresse;
import com.ecom.model.Cart;
import com.ecom.model.CartItem;
import com.ecom.model.Commande;
import com.ecom.model.Products;
import com.ecom.repsitory.AdresseRepsitory;
import com.ecom.repsitory.CartItemRepsitory;
import com.ecom.repsitory.CartRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
import com.ecom.model.Panier;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.service.CartItemService;
import com.ecom.service.CartService;
import com.ecom.service.CommandeService;

import jakarta.servlet.http.HttpSession;
@Controller
public class helloController {
	@Autowired
    private UserService userService;
	@Autowired
    private ProductService productService;
	@Autowired
    private CartItemService cartItemService;
	@Autowired
    private CartService cartService;
	@Autowired
    private CommandeService commandeService;
	 
	@GetMapping("/")
	public String index(Model model) {
		List<Products> products = productService.getAllProducts();
        model.addAttribute("products", products);
        List<Products> latestProducts = productService.getLatestProducts();
        model.addAttribute("latestProducts", latestProducts);
		return "index";
	}
	
		
		@GetMapping("/products")
		public String products(Model model) {
			List<Products> products = productService.getAllProducts();
	        model.addAttribute("products", products);
			return "products";
		}
		@GetMapping("/index/products")
	    public String productsUnderIndex() {
	        return "products";
	    }
		
		@GetMapping("/product-details")
		public String product(Model model,@RequestParam Long productid) {
			Optional<Products> pr = productService.productdetails(productid);
			if (pr.isPresent()) {
		        model.addAttribute("product_detail", pr.get());
		    } else {
		        model.addAttribute("product_detail", null);
		    }
			//model.addAttribute("product_deatail", pr);
			return "product_details";
		}
		@GetMapping("/Auth")
		public String logorsign(HttpSession session, Model model) {
		    Users loggedUser = (Users) session.getAttribute("loggedUser");

		    if (loggedUser != null) {
		        model.addAttribute("user", loggedUser);
		        return "redirect:/";
		    } else {
		    	return "logorsign"; // Si l'utilisateur n'est pas connecté
			
		}
		   
}
		@GetMapping("/Admin")
	    public String adminHome(HttpSession session, Model model) {
	        String role = (String) session.getAttribute("role");
	        if ("admin".equals(role)) {
	            return "Admin"; // Retourne la vue admin
	        } else {
	            model.addAttribute("error", "Accès refusé");
	            return "login"; // Redirige vers la page de connexion
	        }
	   

}
		@GetMapping("/logout")
			public String logout(HttpSession session, Model model) {
		        	session.invalidate();
		            return "redirect:/"; // Redirige vers la page de connexion
		    }
		
		@PostMapping("/AddToCart")
		public String AddToCart(Model model, @RequestParam Long productid, HttpSession session,@RequestParam int qte) {
		    if (session != null && session.getAttribute("loggedUser") != null) {
		        CartItem cartItem = new CartItem();
		        

		        Users user = (Users) session.getAttribute("loggedUser");
		        Cart cart = cartService.getCartByUserId(user.getId()); 
		        Products product = productService.productdetails(productid).get();
		        if (cartItemService.getCartItem(cart.getCartId(),productid)!=null){
		        	cartItemService.updateCartItemQuantity(cart.getCartId(), productid, qte);
		        	productService.updateProductQuantity(productid,qte);	
		        }
		        else {
		        	cartItem.setCart(cart); 
			        cartItem.setProduct(product);
			        cartItem.setQuantity(qte); 
			        productService.updateProductQuantity(productid,qte);
			        cartItemService.ajoutpannier(cartItem); 
		        }

		        
		        
		    
		        return "redirect:/";
		    } else {
		        return "redirect:/Auth/login"; 
		    }
		}
		
		@GetMapping("/cart")
		public String cart(Model model,HttpSession session) {
			 if (session != null && session.getAttribute("loggedUser") != null) {
				 	Users user = (Users) session.getAttribute("loggedUser");
			        Cart cart = cartService.getCartByUserId(user.getId());
			   
			        List<Panier> products = cartItemService.getProductsInCart(user.getId());
			        Double p=cartItemService.calculsomme(products);
	                model.addAttribute("pro", products);
	                model.addAttribute("p", p);
			        
			        return "CartNoAcc";
			    } else {
			        return "Cart"; 
			    }
		}
		@GetMapping("/ModeExpedition")
	    public String modeexpedetion(Model model,HttpSession session) {
			Users user = (Users) session.getAttribute("loggedUser");
			List<Adresse> adr = userService.getAdressesForUser( user.getId());
			model.addAttribute("x", adr);
	        return "ModeExpedition";
	    }
		@GetMapping("/Adda")
	    public String adda(Model model,HttpSession session) {

	        return "AddAdress";
	    }
		
		@GetMapping("/orders")
	    public String showorders(Model model,HttpSession session) {
			Users user = (Users) session.getAttribute("loggedUser");
			List<Commande> cmd= commandeService.getOrdersByUserId(user.getId());
			/*List<Adresse> adr = userService.getAdressesForUser( user.getId());*/
			model.addAttribute("cmd", cmd);
	        return "Orders";
	    }

		
		
}