package com.devtam.adminservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderController {
    @GetMapping("/order-list")
    public String getListOrder() {

        return "order/order-list";
    }

    @GetMapping("/order-details")
    public String orderDetails() {

        return "order/order-details";
    }
}
