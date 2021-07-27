package com.Leapcode.LeapcodeShoppingCart.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/test")
    public String home() {
        return "page";
    }
}
