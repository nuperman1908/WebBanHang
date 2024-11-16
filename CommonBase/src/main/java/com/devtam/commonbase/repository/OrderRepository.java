package com.devtam.commonbase.repository;

import com.devtam.commonbase.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByOrderStatus(int status, Pageable pageable);

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    Page<Order> findAllByUserIdAndOrderStatus(long userId, int status, Pageable pageable);
}
