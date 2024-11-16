package com.devtam.commonbase.service.implement;

import com.devtam.commonbase.entity.Order;
import com.devtam.commonbase.repository.OrderRepository;
import com.devtam.commonbase.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public Order getOrderById(long orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Order> getOrderList(int page, int limit) {
        try {
            List<Order> orderList = orderRepository.findAll(PageRequest.of(page, limit)).getContent();
            return orderList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Order> getOrderList(int page, int limit, int status) {
        try {
            List<Order> orderList = orderRepository.findAllByOrderStatus(status, PageRequest.of(page, limit)).getContent();
            return orderList;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Page<Order> getOrderPage(int page, int limit) {
        try {
            Page<Order> orderPage = orderRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> getOrderPageByUser(long userId, int page, int limit) {
        try {
            Page<Order> orderPage = orderRepository.findAllByUserId(userId, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> getOrderPageByUserAndStatus(long userId, int page, int limit, int status) {
        try {
            Page<Order> orderPage = orderRepository.findAllByUserIdAndOrderStatus(userId, status, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "orderId")));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Page<Order> getOrderPage(int page, int limit, int status) {
        try {
            Page<Order> orderPage = orderRepository.findAllByOrderStatus(status, PageRequest.of(page, limit));
            return orderPage;
        } catch (Exception e) {
            return Page.empty();
        }
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public boolean deleteOrder(Order order) {
        try {
            orderRepository.delete(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteOrder(long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
