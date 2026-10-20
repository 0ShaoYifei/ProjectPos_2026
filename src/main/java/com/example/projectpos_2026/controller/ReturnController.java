package com.example.projectpos_2026.controller;

import com.example.projectpos_2026.entity.ReturnRecord;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.service.ReturnService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReturnController {
    private final ReturnService returns;
    public ReturnController(ReturnService returns) { this.returns = returns; }
    @PostMapping("/sales/{saleId}/returns")
    public String process(@PathVariable Long saleId, @RequestParam Long itemId, @RequestParam int quantity,
                          @RequestParam String reason, @RequestParam String requestKey, RedirectAttributes redirect) {
        try {
            ReturnRecord result = returns.process(saleId, itemId, quantity, reason, requestKey);
            redirect.addFlashAttribute("message", "Return recorded. Cash refund: CNY " + result.getRefund() + ". Inventory updated.");
        } catch (BusinessException ex) { redirect.addFlashAttribute("error", ex.getMessage()); }
        return "redirect:/sales/" + saleId;
    }
}
