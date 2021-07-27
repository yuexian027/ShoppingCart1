package com.Leapcode.LeapcodeShoppingCart.controllers;

import com.Leapcode.LeapcodeShoppingCart.models.PageRepository;
import com.Leapcode.LeapcodeShoppingCart.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepo;

    @GetMapping
    public String index(Model model) {
        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        model.addAttribute("pages", pages);
        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("page", new Page());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if (bindingResult.hasErrors()) {
            return "admin/pages/add";
        }

        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        String casing = page.getCasing() == "" ? page.getTitle().toLowerCase().replace(" ", "-") :
                page.getCasing().toLowerCase().replace(" ", "-");
        Page casingExists = pageRepo.findByCasing(casing);
        if (casingExists != null){
            redirectAttributes.addFlashAttribute("message", "Casing exists, choose another name");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setCasing(casing);
            page.setSorting(100);
            pageRepo.save(page);
        }
        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam(value = "id") int id , Model model) {
        Page page = pageRepo.getOne(id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/pages/edit";
        }

        redirectAttributes.addFlashAttribute("message", "Page edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        String casing = page.getCasing() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getCasing().toLowerCase().replace(" ", "-");

        Page casingExists = pageRepo.findByCasing(page.getId(), casing);

        if (casingExists != null) {
            redirectAttributes.addFlashAttribute("message", "Casing exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setCasing(casing);

            pageRepo.save(page);
        }
        return "redirect:/admin/pages/edit/" + page.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        pageRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {
        int count = 1;
        Page page;
        for(int pageId : id){
            page = pageRepo.getOne(pageId);
            page.setSorting(count);
            pageRepo.save(page);
            count++;
        }
        return "ok";
    }
    //dsjdksjakjds
}
