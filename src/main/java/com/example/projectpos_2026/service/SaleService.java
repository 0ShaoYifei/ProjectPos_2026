package com.example.projectpos_2026.service;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@Service @Transactional(readOnly = true)
public class SaleService {
    private final ProductRepository products;
    private final SaleRepository sales;
    public SaleService(ProductRepository products, SaleRepository sales) { this.products = products; this.sales = sales; }
    public List<Sale> all() { return sales.findAllByOrderByCreatedAtDesc(); }
    public Sale get(Long id) { return sales.findDetailedById(id).orElseThrow(() -> new BusinessException("Receipt not found.")); }
    public record CartLine(Long productId, String name, String barcode, BigDecimal unitPrice, int quantity, BigDecimal subtotal) { }
    public List<CartLine> preview(Map<Long, Integer> quantities) {
        List<CartLine> lines = new ArrayList<>();
        for (var entry : quantities.entrySet()) {
            Product p = products.findById(entry.getKey()).orElseThrow(() -> new BusinessException("A product in the cart no longer exists."));
            lines.add(new CartLine(p.getId(), p.getName(), p.getBarcode(), p.getPrice(), entry.getValue(), p.getPrice().multiply(BigDecimal.valueOf(entry.getValue()))));
        }
        return lines;
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Sale checkout(Map<Long, Integer> quantities, BigDecimal cash, String checkoutKey) {
        validateKey(checkoutKey);
        Optional<Sale> existing = sales.findByCheckoutKey(checkoutKey);
        if (existing.isPresent()) return existing.get();
        if (quantities == null || quantities.isEmpty()) throw new BusinessException("Add at least one product before taking payment.");
        if (quantities.size() > 100 || quantities.entrySet().stream().anyMatch(e -> e.getKey() == null || e.getValue() == null || e.getValue() < 1 || e.getValue() > 10000))
            throw new BusinessException("Use at most 100 products and quantities between 1 and 10000.");
        if (cash == null || cash.signum() < 0 || cash.stripTrailingZeros().scale() > 2 || cash.compareTo(new BigDecimal("999999999.99")) > 0)
            throw new BusinessException("Enter a valid cash payment with at most two decimal places.");
        Sale sale = new Sale(checkoutKey);
        Map<Long, Product> locked = new TreeMap<>();
        // A consistent product lock order avoids opposite-order checkout deadlocks.
        for (Long id : new TreeSet<>(quantities.keySet())) {
            Product product = products.findLockedById(id).orElseThrow(() -> new BusinessException("Product not found."));
            if (!product.isActive()) throw new BusinessException("Product " + product.getName() + " is inactive.");
            if (product.getStock() < quantities.get(id)) throw new BusinessException("Insufficient stock for " + product.getName() + ".");
            locked.put(id, product); sale.addItem(product, quantities.get(id));
        }
        // Recheck after waiting on inventory locks to make repeated checkout safe.
        existing = sales.findByCheckoutKey(checkoutKey);
        if (existing.isPresent()) return existing.get();
        if (sale.getTotal().compareTo(new BigDecimal("999999999.99")) > 0) throw new BusinessException("The sale total exceeds the supported cash limit.");
        if (cash.compareTo(sale.getTotal()) < 0) throw new BusinessException("Insufficient payment. Cash must cover the sale total.");
        sale.recordPayment(cash);
        locked.forEach((id, product) -> product.decreaseStock(quantities.get(id)));
        return sales.saveAndFlush(sale);
    }
    public static void validateKey(String key) {
        try { if (key == null || !UUID.fromString(key).toString().equals(key)) throw new IllegalArgumentException(); }
        catch (IllegalArgumentException ex) { throw new BusinessException("This form has expired. Reload the page and try again."); }
    }
}
