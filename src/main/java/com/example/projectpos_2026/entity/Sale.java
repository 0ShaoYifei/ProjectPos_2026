package com.example.projectpos_2026.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Entity @Table(name = "sales")
public class Sale {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 36) private String checkoutKey;
    @Column(nullable = false) private LocalDateTime createdAt;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal total;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal cashPaid;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal changeDue;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL) @OrderBy("id ASC")
    private List<SaleItem> items = new ArrayList<>();
    protected Sale() { }
    public Sale(String checkoutKey) {
        this.checkoutKey = checkoutKey; createdAt = LocalDateTime.now(ZoneId.of("Asia/Shanghai")); total = new BigDecimal("0.00");
    }
    public void addItem(Product product, int quantity) {
        SaleItem item = new SaleItem(this, product, quantity); items.add(item); total = total.add(item.getSubtotal());
    }
    public void recordPayment(BigDecimal cash) { cashPaid = cash.setScale(2); changeDue = cashPaid.subtract(total); }
    public Long getId() { return id; }
    public String getCheckoutKey() { return checkoutKey; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getCashPaid() { return cashPaid; }
    public BigDecimal getChangeDue() { return changeDue; }
    public List<SaleItem> getItems() { return Collections.unmodifiableList(items); }
    public BigDecimal getRefundedTotal() { return items.stream().map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getReturnedQuantity()))).reduce(new BigDecimal("0.00"), BigDecimal::add); }
}
