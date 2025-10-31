package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsPageController {
    @GetMapping("/products")
    public String products() {
        return "products-catalog";
    }
}
