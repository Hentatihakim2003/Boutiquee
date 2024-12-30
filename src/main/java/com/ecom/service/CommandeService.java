package com.ecom.service;

import com.ecom.model.Commande;
import com.ecom.repsitory.CommandeRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepsitory commandeRepsitory;

    public Commande createOrder(Commande order) {
        return commandeRepsitory.save(order);
    }

    public Optional<Commande> getOrderById(Long orderId) {
        return commandeRepsitory.findById(orderId);
    }

    public List<Commande> getOrdersByUserId(Long userId) {
        return commandeRepsitory.findByUser_Id(userId);
    }

    public Commande updateOrderStatus(Long orderId, Commande.Status status) {
        Optional<Commande> orderOptional = commandeRepsitory.findById(orderId);
        if (orderOptional.isPresent()) {
        	Commande order = orderOptional.get();
            order.setStatus(status);
            return commandeRepsitory.save(order);
        }
        throw new RuntimeException("Order not found with id: " + orderId);
    }

    // Supprimer une commande
    public void deleteOrder(Long orderId) {
    	commandeRepsitory.deleteById(orderId);
    }
    public void updateStatus(Long orderId, Commande.Status status) {
        Commande commande = commandeRepsitory.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée pour l'ID : " + orderId));
        commande.setStatus(status);
        commandeRepsitory.save(commande);
    }
    public List<Commande> getAllCommandes() {
        return commandeRepsitory.findAll();
    }
}
