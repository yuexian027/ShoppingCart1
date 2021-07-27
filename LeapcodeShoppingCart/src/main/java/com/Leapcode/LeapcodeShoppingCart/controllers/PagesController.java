package com.Leapcode.LeapcodeShoppingCart.controllers;

import com.Leapcode.LeapcodeShoppingCart.models.PageRepository;
import com.Leapcode.LeapcodeShoppingCart.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;

import jdk.jfr.Registered;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PagesController {
    @Autowired

    private PageRepository pageRepo;

    @GetMapping
    public String home(Model model){
        Page page = pageRepo.findByCasing("home");
        model.addAttribute("page",page);
        return "page";

    }
    @GetMapping("/{casing}")
    public String page(Model model, @PathVariable String casing){
        Page page = pageRepo.findByCasing(casing);
        if(page == null) {
            return "redirect:/";
        }
        model.addAttribute("page",page);
        return "page";

    }

}
