package com.devtam.adminservice.controller;

import com.devtam.commonbase.entity.Category;
import com.devtam.commonbase.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/get-category-children/{parentId}")
    @ResponseBody
    public List<Category> getCategoryChildren(@PathVariable("parentId") long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        
        return categories;
    }
}
