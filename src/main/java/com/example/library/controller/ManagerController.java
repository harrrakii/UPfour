package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "manager-main";
    }

    @GetMapping("/products")
    public String products(Model model) {
        return "admin/products-admin";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        return "admin/orders-main";
    }
}

