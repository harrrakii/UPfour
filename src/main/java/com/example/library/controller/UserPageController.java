package com.example.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/user/dashboard")
    public String dashboard() {
        return "user-main"; // ✅ имя файла, который реально существует
    }
}

