package com.example.projectpos_2026.controller;

import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.entity.Product;
import com.example.projectpos_2026.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {
    private final ProductService products;
    public ProductController(ProductService products) { this.products = products; }
    @GetMapping("/") public String home() { return "redirect:/products"; }
    @GetMapping("/products") public String list(Model model) { model.addAttribute("products", products.all()); return "products"; }
    @GetMapping("/products/new") public String create(Model model) { model.addAttribute("productForm", new ProductForm()); return "product-form"; }
    @GetMapping("/products/{id}/edit") public String edit(@PathVariable Long id, Model model) {
        Product p = products.get(id); ProductForm form = new ProductForm();
        form.setBarcode(p.getBarcode()); form.setName(p.getName()); form.setPrice(p.getPrice());
        form.setStock(p.getStock()); form.setActive(p.isActive()); form.setVersion(p.getVersion());
        model.addAttribute("productForm", form); model.addAttribute("productId", id); return "product-form";
    }
    @PostMapping("/products/save")
    public String save(@RequestParam(required = false) Long id, @Valid @ModelAttribute ProductForm productForm,
                       BindingResult result, Model model, RedirectAttributes redirect) {
        model.addAttribute("productId", id);
        if (result.hasErrors()) return "product-form";
        try {
            if (id == null) products.create(productForm.getBarcode(), productForm.getName(), productForm.getPrice(), productForm.getStock());
            else products.update(id, productForm.getVersion(), productForm.getBarcode(), productForm.getName(), productForm.getPrice(), productForm.getStock(), productForm.isActive());
        } catch (BusinessException ex) { result.reject("business", ex.getMessage()); return "product-form"; }
        redirect.addFlashAttribute("message", "Product saved."); return "redirect:/products";
    }
}
