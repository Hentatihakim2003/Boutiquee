package com.ecom.repsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.ecom.model.Users;
import com.ecom.model.Products;
@Repository
public interface ProductRepsitory extends JpaRepository<Products, Long> {
    Optional<Products> findById(Long id);
    List<Products> findTop4ByOrderByIdDesc();
    List<Products> findAll();
    
}