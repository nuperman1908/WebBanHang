package com.devtam.userservice.controller;

import com.devtam.adminservice.common.PasswordHandler;
import com.devtam.adminservice.controller.HomeController;
import com.devtam.commonbase.constant.ImageTypes;
import com.devtam.commonbase.entity.Account;
import com.devtam.commonbase.entity.Category;
import com.devtam.commonbase.entity.Image;
import com.devtam.commonbase.entity.User;
import com.devtam.commonbase.service.AccountService;
import com.devtam.commonbase.service.CategoryService;
import com.devtam.commonbase.service.ImageService;
import com.devtam.commonbase.service.UserService;
import com.devtam.commonbase.util.RoleConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    HttpSession session;
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    ImageService imageService;
    private final Logger _log = LogManager.getLogger(AccountController.class);

    @GetMapping("/login")
    public String loginUser(Authentication authentication, @RequestParam(value = "error", defaultValue = "") String error, Model model
            , @RequestParam(value = "login-error", defaultValue = "") String loginError) {
        getCategoryParentList();
        getUserData(authentication);
        if (error.equals("true")) {
            model.addAttribute("error", "true");
        }
        if (loginError.equals("true")) {
            model.addAttribute("loginError", "true");
        }
        if (authentication != null) {
            return "redirect:/home?logged-in=true";
        }
        return "account/login";
    }

    @GetMapping("/sign-up")
    public String signUpUser(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/sign-up?logged-in=true";
        }
        getCategoryParentList();
        getUserData(authentication);
        return "account/sign-up";
    }

    @PostMapping("sign-up")
    public String registerNewUser(@ModelAttribute("full-name") String fullName, @ModelAttribute("email") String email
            , @ModelAttribute("phone-number") String phoneNumber, @ModelAttribute("password") String password, @ModelAttribute("date-of-birth") Date dateOfBirth) {
        Account account = Account.builder().email(email).phoneNumber(phoneNumber)
                .password(PasswordHandler.hashPassword(password))
                .role(RoleConstant.USER.getValue()).build();
        Account savedAccount = accountService.saveAccount(account);
        User user = User.builder().userId(savedAccount.getAccountId()).dateOfBirth(dateOfBirth).name(fullName).build();
        User savedUser = userService.saveUser(user);
        return "redirect:/login?success=true";
    }

    @GetMapping("/personal-information")
    public String personalInformation(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login?error=true";
        }
        getCategoryParentList();
        getUserData(authentication);
        return "account/personal";
    }

    private void getCategoryParentList() {
        List<Category> categoryList = categoryService.getCategoriesByParentId(-1);
        if (session.getAttribute("categoryParents") == null) {
            if (categoryList != null && !categoryList.isEmpty()) {
                session.setAttribute("categoryParents", categoryList);
            }
        }
        if (session.getAttribute("imageMap") == null) {
            if (categoryList != null && !categoryList.isEmpty()) {
                List<Long> categoryIds = categoryList.stream().map(Category::getCategoryId).collect(Collectors.toList());
                Map<Long, Image> imageMap = imageService.getMapOneImage(categoryIds, ImageTypes.CATEGORY_IMG.getValue());
                if (imageMap != null && !imageMap.isEmpty()) {
                    session.setAttribute("imageMap", imageMap);
                }
            }
        }
    }

    private void getUserData(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Lấy userId từ UserDetails
            if (session.getAttribute("user") == null) {
                User user = userService.getUserById(Long.valueOf(userId));
                if (user.getName() == null) {
                    user.setName("");
                }
                session.setAttribute("user", user);
            }
            if (session.getAttribute("image") == null) {
                List<Image> imageList = imageService.getListImage(Long.valueOf(userId), ImageTypes.USER_IMG.getValue());
                if (imageList != null && !imageList.isEmpty()) {
                    session.setAttribute("image", imageList.get(0));
                }
            }

        } catch (Exception e) {
            _log.error(e.getMessage());
        }
    }

}
