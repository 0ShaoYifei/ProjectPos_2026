package com.example.projectpos_2026.entity;

import com.example.projectpos_2026.exception.BusinessException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name = "sale_items")
public class SaleItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "sale_id", nullable = false) private Sale sale;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_id", nullable = false) private Product product;
    @Column(nullable = false, length = 100) private String productName;
    @Column(nullable = false, length = 40) private String barcode;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal unitPrice;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false) private int returnedQuantity;
    protected SaleItem() { }
    public SaleItem(Sale sale, Product product, int quantity) {
        if (quantity <= 0 || quantity > 10000) throw new BusinessException("Quantity must be between 1 and 10000.");
        this.sale = sale; this.product = product; this.quantity = quantity;
        productName = product.getName(); barcode = product.getBarcode(); unitPrice = product.getPrice();
    }
    public void recordReturn(int quantity) {
        if (quantity <= 0 || quantity > getReturnableQuantity())
            throw new BusinessException("Return quantity exceeds the remaining purchased quantity.");
        returnedQuantity += quantity;
    }
    public Long getId() { return id; }
    public Sale getSale() { return sale; }
    public Product getProduct() { return product; }
    public String getProductName() { return productName; }
    public String getBarcode() { return barcode; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public int getReturnedQuantity() { return returnedQuantity; }
    public int getReturnableQuantity() { return quantity - returnedQuantity; }
    public BigDecimal getSubtotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
