package com.ecom.repsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecom.model.Cart;
import com.ecom.model.CartItem;

@Repository

public interface CartItemRepsitory extends JpaRepository<CartItem, Long> {
	List<CartItem> findByCart_User_Id(Long userId);
	CartItem findByCart_CartIdAndProduct_Id(Long cartId, Long productId);
	void deleteByCart(Cart cart);
}
