package com.example.projectpos_2026.entity;

import com.example.projectpos_2026.exception.BusinessException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "barcode"))
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version private Long version;
    @Column(nullable = false, length = 40) private String barcode;
    @Column(nullable = false, length = 100) private String name;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
    @Column(nullable = false) private int stock;
    @Column(nullable = false) private boolean active = true;

    protected Product() { }
    public Product(String barcode, String name, BigDecimal price, int stock) {
        update(barcode, name, price, stock, true);
    }
    public void update(String barcode, String name, BigDecimal price, int stock, boolean active) {
        if (barcode == null || !barcode.trim().matches("[A-Za-z0-9-]{1,40}"))
            throw new BusinessException("Barcode must contain 1 to 40 letters, numbers or hyphens.");
        if (name == null || name.isBlank() || name.trim().length() > 100)
            throw new BusinessException("Product name must contain 1 to 100 characters.");
        if (price == null || price.signum() <= 0 || price.compareTo(new BigDecimal("999999.99")) > 0
                || price.stripTrailingZeros().scale() > 2)
            throw new BusinessException("Price must be between 0.01 and 999999.99 with at most two decimal places.");
        if (stock < 0 || stock > 1_000_000)
            throw new BusinessException("Stock must be between 0 and 1000000.");
        this.barcode = barcode.trim(); this.name = name.trim();
        this.price = price.setScale(2); this.stock = stock; this.active = active;
    }
    public void decreaseStock(int quantity) {
        if (!active) throw new BusinessException("This product is inactive.");
        if (quantity <= 0 || stock < quantity) throw new BusinessException("Insufficient stock for " + name + ".");
        stock -= quantity;
    }
    public void restoreStock(int quantity) {
        if (quantity <= 0 || (long) stock + quantity > 1_000_000)
            throw new BusinessException("The return would exceed the inventory limit.");
        stock += quantity;
    }
    public Long getId() { return id; }
    public Long getVersion() { return version; }
    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isActive() { return active; }
}
