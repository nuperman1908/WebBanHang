package com.devtam.commonbase.repository;

import com.devtam.commonbase.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserId(long userId);

    List<Cart> findAllByOrderId(long orderId);

    List<Cart> findAllByUserIdAndPaymentStatus(long userId, int paymentMethod);
}
