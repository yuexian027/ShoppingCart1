package com.Leapcode.LeapcodeShoppingCart.controllers;

import com.Leapcode.LeapcodeShoppingCart.models.CategoryRepository;
import com.Leapcode.LeapcodeShoppingCart.models.ProductRepository;
import com.Leapcode.LeapcodeShoppingCart.models.data.Category;
import com.Leapcode.LeapcodeShoppingCart.models.data.Page;
import com.Leapcode.LeapcodeShoppingCart.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model, @RequestParam(value="page", required=false) Integer p){
        int perPage = 4;
        int page = (p != null) ? p : 0;

        Pageable pageable = PageRequest.of(page, perPage);
        org.springframework.data.domain.Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = categoryRepo.findAll();
        Map<Integer, String> cats = new HashMap<>();
        for(Category cat : categories){
            cats.put(cat.getId(), cat.getName());
        }
        model.addAttribute("cats", cats);
        model.addAttribute("products", products);

        Long count = productRepo.count();
        double pageCount = Math.ceil((double)count / (double)perPage);
        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        return "/admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model){
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "/admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }

        boolean isFileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();

        Path path = Paths.get("E:\\LeapcodeShoppingCart\\LeapcodeShoppingCart\\src\\main\\resources\\static\\media\\" + filename);

        if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg")) {
            isFileOK = true;
        }
        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        String casing = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepo.findByCasing(casing);
        if (!isFileOK){
            redirectAttributes.addFlashAttribute("message", "Image must be in jpg, jpeg or png format");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another name");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            product.setCasing(casing);
            product.setImage(filename);
            productRepo.save(product);
            Files.write(path, bytes);
        }
        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productRepo.getOne(id);
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product,
                      BindingResult bindingResult,
                      MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {
        Product currentProduct = productRepo.getOne(product.getId());
        if (bindingResult.hasErrors()) {
            return "admin/product/edit";
        }

        boolean isFileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();

        Path path = Paths.get("E:\\LeapcodeShoppingCart\\LeapcodeShoppingCart\\src\\main\\resources\\static\\media\\" + filename);

        if (!file.isEmpty()){
            if (filename.endsWith("jpg") || filename.endsWith("png") || filename.endsWith("jpeg")) {
                isFileOK = true;
            }
        } else {
            isFileOK = true;
        }
        redirectAttributes.addFlashAttribute("message", "Product edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        String casing = product.getName().toLowerCase().replace(" ", "-");
        Product productExists = productRepo.findByCasingAndIdNot(casing, product.getId());

        if (!isFileOK){
            redirectAttributes.addFlashAttribute("message", "Image must be in jpg, jpeg or png format");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another name");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            product.setCasing(casing);
            if (!file.isEmpty()){
                Path path2 = Paths.get("E:\\LeapcodeShoppingCart\\LeapcodeShoppingCart\\src\\main\\resources\\static\\media\\" + currentProduct.getImage());
                Files.delete(path2);
                product.setImage(filename);
                Files.write(path, bytes);
            } else {
                product.setImage(currentProduct.getImage());
            }

            productRepo.save(product);
        }
        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {
        Product product = productRepo.getOne(id);
        productRepo.deleteById(id);
        Path path = Paths.get("E:\\LeapcodeShoppingCart\\LeapcodeShoppingCart\\src\\main\\resources\\static\\media\\" + product.getImage());
        Files.delete(path);
        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/admin/products";
    }
}
