package com.devtam.userservice.controller;

import com.devtam.commonbase.entity.Cart;
import com.devtam.commonbase.entity.Comment;
import com.devtam.commonbase.entity.Order;
import com.devtam.commonbase.service.CartService;
import com.devtam.commonbase.service.CommentService;
import com.devtam.commonbase.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;

    @PostMapping("/add-comment/{orderId}")
    public String addComment(HttpServletRequest request, Authentication authentication,
                             @PathVariable("orderId") long orderId,
                             @ModelAttribute("summary") String summary,
                             @RequestParam(value = "star", defaultValue = "5") int rating,
                             @ModelAttribute("comment") String comments) {
        String userId = "-1";
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return "redirect:/login?error=true";
        }
        userId = userDetails.getUsername();
        Order order = orderService.getOrderById(orderId);
        if (order == null || order.isCommented()) {
            return "redirect:/error";
        }
        List<Cart> cartList = cartService.getCartListByOrder(orderId);
        for (Cart cart : cartList) {
            Comment comment = Comment.builder().summary(summary).rating(rating).comments(comments).userId(Long.parseLong(userId)).productId(cart.getProductId()).build();
            commentService.addComment(comment);
        }
        order.setCommented(true);
        orderService.saveOrder(order);
        String returnUrl = request.getHeader("Referer");
        if (returnUrl == null) {
            returnUrl = "/home";
        }
        return "redirect:" + returnUrl;
    }
}
