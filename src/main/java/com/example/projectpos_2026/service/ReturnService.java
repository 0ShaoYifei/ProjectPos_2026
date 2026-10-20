package com.example.projectpos_2026.service;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service @Transactional(readOnly = true)
public class ReturnService {
    private final SaleItemRepository items;
    private final ProductRepository products;
    private final ReturnRepository returns;
    public ReturnService(SaleItemRepository items, ProductRepository products, ReturnRepository returns) {
        this.items = items; this.products = products; this.returns = returns;
    }
    public record ReturnView(Long id, String productName, int quantity, BigDecimal refund, String reason, LocalDateTime createdAt) { }
    public List<ReturnView> forSale(Long saleId) {
        return returns.findBySaleItemSaleIdOrderByCreatedAtDesc(saleId).stream()
                .map(r -> new ReturnView(r.getId(), r.getSaleItem().getProductName(), r.getQuantity(), r.getRefund(), r.getReason(), r.getCreatedAt())).toList();
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReturnRecord process(Long saleId, Long itemId, int quantity, String reason, String requestKey) {
        SaleService.validateKey(requestKey);
        Optional<ReturnRecord> existing = returns.findByRequestKey(requestKey);
        if (existing.isPresent()) return existing.get();
        if (quantity < 1 || quantity > 10000) throw new BusinessException("Return quantity must be between 1 and 10000.");
        if (reason == null || reason.isBlank() || reason.trim().length() > 200) throw new BusinessException("Enter a return reason of 1 to 200 characters.");
        SaleItem item = items.findLocked(itemId, saleId).orElseThrow(() -> new BusinessException("The purchased item was not found on this receipt."));
        existing = returns.findByRequestKey(requestKey);
        if (existing.isPresent()) return existing.get();
        if (quantity > item.getReturnableQuantity()) throw new BusinessException("Return quantity exceeds the remaining purchased quantity.");
        Product product = products.findLockedById(item.getProduct().getId()).orElseThrow(() -> new BusinessException("Product not found."));
        // Both updates and the refund record commit together, or all roll back.
        product.restoreStock(quantity); item.recordReturn(quantity);
        return returns.saveAndFlush(new ReturnRecord(requestKey, item, quantity, reason));
    }
}
