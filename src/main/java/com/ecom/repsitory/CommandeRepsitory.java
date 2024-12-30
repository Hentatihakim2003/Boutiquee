package com.ecom.repsitory;

import com.ecom.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepsitory extends JpaRepository<Commande, Long> {
    
    List<Commande> findByUser_Id(Long userId);

    
    List<Commande> findByStatus(Commande.Status status);
}
