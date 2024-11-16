package com.devtam.adminservice.controller;

import com.devtam.commonbase.entity.User;
import com.devtam.commonbase.service.AccountService;
import com.devtam.commonbase.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class HomeController {
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    HttpSession session;
    private final Logger _log = LogManager.getLogger(HomeController.class);

    @GetMapping({"/", "/home", ""})
    public String home(Model model, Authentication authentication) {
        model.addAttribute("image_url", "/img/profile-pic.png");
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Lấy userId từ UserDetails
            User user = userService.getUserById(Long.valueOf(userId));
            if (user.getName() == null) {
                user.setName("");
            }
            session.setAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


}
