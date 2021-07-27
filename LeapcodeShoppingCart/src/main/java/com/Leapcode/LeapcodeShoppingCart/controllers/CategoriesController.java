package com.Leapcode.LeapcodeShoppingCart.controllers;

import com.Leapcode.LeapcodeShoppingCart.models.CategoryRepository;
import com.Leapcode.LeapcodeShoppingCart.models.ProductRepository;
import com.Leapcode.LeapcodeShoppingCart.models.data.Category;
import com.Leapcode.LeapcodeShoppingCart.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.GroupSequence;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoriesController {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/{casing} ")
    public String category(@PathVariable String casing, Model model, @RequestParam(value="page", required=false) Integer p){
        int perPage = 4;
        int page = (p != null) ? p : 0;

        Pageable pageable = PageRequest.of(page, perPage);

        long count = 0;
        if(casing.equals("all")){
            org.springframework.data.domain.Page<Product> products = productRepo.findAll(pageable);
            count = productRepo.count();
            model.addAttribute("product",products);
        }
        else{
            Category category = categoryRepo.findByCasing(casing );
            if(category == null){
                return "redirect:/";
            }
            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Product> products = productRepo.findAllByCategoryId(categoryId,pageable);

            count = products.size();

            model.addAttribute("products",products);
            model.addAttribute("categoryName", categoryName);


        }
        double pageCount = Math.ceil((double)count / (double)perPage);
        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "products";
    }

}
