package com.example.projectpos_2026.controller;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.UUID;

@Controller
public class SaleController {
    private final ProductService products;
    private final SaleService sales;
    private final ReturnService returns;
    public SaleController(ProductService products, SaleService sales, ReturnService returns) { this.products = products; this.sales = sales; this.returns = returns; }
    private ShoppingCart cart(HttpSession session) {
        synchronized (session) {
            ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
            if (cart == null) { cart = new ShoppingCart(); session.setAttribute("cart", cart); }
            return cart;
        }
    }
    @GetMapping("/sales/new") public String newSale(HttpSession session, Model model) {
        ShoppingCart cart = cart(session);
        synchronized (cart) {
            var lines = sales.preview(cart.items());
            model.addAttribute("lines", lines); model.addAttribute("checkoutKey", cart.getCheckoutKey());
            model.addAttribute("total", lines.stream().map(SaleService.CartLine::subtotal).reduce(new BigDecimal("0.00"), BigDecimal::add));
        }
        model.addAttribute("products", products.active()); return "sale-form";
    }
    @PostMapping("/sales/cart/add") public String add(@RequestParam Long productId, @RequestParam int quantity, HttpSession session, RedirectAttributes redirect) {
        try {
            Product product = products.get(productId);
            if (!product.isActive()) throw new BusinessException("This product is inactive.");
            cart(session).add(productId, quantity, product.getStock());
        } catch (BusinessException ex) { redirect.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/sales/new";
    }
    @PostMapping("/sales/cart/remove") public String remove(@RequestParam Long productId, HttpSession session) { cart(session).remove(productId); return "redirect:/sales/new"; }
    @PostMapping("/sales/cart/clear") public String clear(HttpSession session) { cart(session).clear(); return "redirect:/sales/new"; }
    @PostMapping("/sales/checkout") public String checkout(@RequestParam BigDecimal cash, @RequestParam String checkoutKey, HttpSession session, RedirectAttributes redirect) {
        ShoppingCart cart = cart(session);
        synchronized (cart) {
            try {
                if (!cart.getCheckoutKey().equals(checkoutKey)) throw new BusinessException("The cart changed or was already paid. Review your transactions before trying again.");
                Sale sale = sales.checkout(cart.items(), cash, checkoutKey); cart.clear();
                redirect.addFlashAttribute("message", "Cash payment recorded. Sale completed."); return "redirect:/sales/" + sale.getId();
            } catch (BusinessException ex) { redirect.addFlashAttribute("error", ex.getMessage()); return "redirect:/sales/new"; }
        }
    }
    @GetMapping("/sales") public String list(Model model) { model.addAttribute("sales", sales.all()); return "sales"; }
    @GetMapping("/sales/lookup") public String lookup(@RequestParam Long id) { sales.get(id); return "redirect:/sales/" + id; }
    @GetMapping("/sales/{id}") public String receipt(@PathVariable Long id, Model model) {
        model.addAttribute("sale", sales.get(id)); model.addAttribute("returnKey", UUID.randomUUID().toString());
        model.addAttribute("returns", returns.forSale(id)); return "receipt";
    }
}
