package com.ecom.repsitory;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.ecom.model.Users;
import com.ecom.model.Products;
import com.ecom.model.Cart;

@Repository
public interface CartRepsitory extends JpaRepository<Cart, Long> {
	Cart findByUserId(Long userid);

}
