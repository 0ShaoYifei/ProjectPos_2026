package com.example.projectpos_2026.service;

import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.entity.Product;
import com.example.projectpos_2026.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository products;
    public ProductService(ProductRepository products) { this.products = products; }
    public List<Product> all() { return products.findAllByOrderByNameAsc(); }
    public List<Product> active() { return products.findByActiveTrueOrderByNameAsc(); }
    public Product get(Long id) { return products.findById(id).orElseThrow(() -> new BusinessException("Product not found.")); }
    @Transactional
    public Product create(String barcode, String name, BigDecimal price, int stock) {
        Product product = new Product(barcode, name, price, stock);
        if (products.findByBarcode(product.getBarcode()).isPresent()) throw new BusinessException("This barcode is already in use.");
        return products.saveAndFlush(product);
    }
    @Transactional
    public Product update(Long id, Long expectedVersion, String barcode, String name, BigDecimal price, int stock, boolean active) {
        Product product = products.findLockedById(id).orElseThrow(() -> new BusinessException("Product not found."));
        if (!Objects.equals(product.getVersion(), expectedVersion))
            throw new BusinessException("This product changed while you were editing it. Reload the page and try again.");
        products.findByBarcode(barcode == null ? "" : barcode.trim()).filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new BusinessException("This barcode is already in use."); });
        product.update(barcode, name, price, stock, active);
        return products.saveAndFlush(product);
    }
}
