package com.ecom.repsitory;



import com.ecom.model.Commandeitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeitemRepsitory extends JpaRepository<Commandeitem, Long> {
    // Trouver tous les OrderItems par orderId
    List<Commandeitem> findByCommandeOrderId(Long orderId);
}
