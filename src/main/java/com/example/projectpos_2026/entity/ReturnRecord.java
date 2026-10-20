package com.example.projectpos_2026.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name = "return_records")
public class ReturnRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 36) private String requestKey;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "sale_item_id", nullable = false) private SaleItem saleItem;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal refund;
    @Column(nullable = false, length = 200) private String reason;
    @Column(nullable = false) private LocalDateTime createdAt;
    protected ReturnRecord() { }
    public ReturnRecord(String key, SaleItem item, int quantity, String reason) {
        requestKey = key; saleItem = item; this.quantity = quantity; this.reason = reason.trim();
        refund = item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        createdAt = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
    }
    public Long getId() { return id; }
    public SaleItem getSaleItem() { return saleItem; }
    public int getQuantity() { return quantity; }
    public BigDecimal getRefund() { return refund; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
