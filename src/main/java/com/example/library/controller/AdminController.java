package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin-main";
    }

    @GetMapping("/users")
    public String users(Model model) {
        return "admin/users-admin";
    }

    @GetMapping("/products")
    public String products(Model model) {
        return "admin/products-admin";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        return "admin/categories-admin";
    }

    @GetMapping("/publishers")
    public String publishers(Model model) {
        return "admin/publishers-admin";
    }


    @GetMapping("/orders")
    public String orders(Model model) {
        return "admin/orders-main";
    }

    @GetMapping("/reviews")
    public String reviews(Model model) {
        return "admin/reviews-admin";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "admin/cart-admin";
    }

    @GetMapping("/roles")
    public String roles(Model model) {
        return "admin/roles-admin";
    }
}

