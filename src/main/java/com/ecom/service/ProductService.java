package com.ecom.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.ecom.model.Users;
import com.ecom.repsitory.UserRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.CartItem;
import com.ecom.repsitory.AdresseRepsitory;
import com.ecom.repsitory.ProductRepsitory;
import com.ecom.model.Adresse;
import com.ecom.model.Users;
import com.ecom.model.Products;
@Service
public class ProductService {

    @Autowired
    private UserRepsitory userRepsitory;

    @Autowired
    private AdresseRepsitory adresseRepsitory;
    @Autowired
    private ProductRepsitory productRepsitory;

    public Products createProduct(Products pr) {
        return productRepsitory.save(pr);
    }
    public List<Products> getAllProducts() {
        return productRepsitory.findAll();
    }

    public List<Products> getLatestProducts() {
        return productRepsitory.findTop4ByOrderByIdDesc();
    }
    public Optional<Products> productdetails(Long id) {
        return productRepsitory.findById(id);
    }
    public Optional<Adresse> adressebyid(Long id) {
        return adresseRepsitory.findById(id);
    }
    
    public void updateProductQuantity(Long productId, int newQuantity) {
        // Rechercher le CartItem correspondant
        //Products pr = productRepsitory.findById(productId);
        Optional<Products> optionalProduct = productRepsitory.findById(productId);
        if (optionalProduct.isPresent()) {
            Products pr = optionalProduct.get();
            int updatedQuantity = pr.getStock() - newQuantity;
            pr.setStock(updatedQuantity);
            productRepsitory.save(pr);
            
            // Traitez le produit ici
        } else {
            throw new RuntimeException("Produit introuvable pour l'ID : " + productId);
        }
        /*if (pr != null) {
        	int updatedQuantity = cartItem.getQuantity() + newQuantity;
            cartItem.setQuantity(updatedQuantity);

            // Sauvegarder les modifications
            cartItemRepsitory.save(cartItem);
        } else {
            throw new RuntimeException("CartItem introuvable pour cartId = " + cartId + " et productId = " + productId);
        }*/
    } 
}