package com.example.projectpos_2026.controller;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductForm {
    @NotBlank(message = "Enter a barcode.")
    @Pattern(regexp = "[A-Za-z0-9-]{1,40}", message = "Use 1 to 40 letters, numbers or hyphens.")
    private String barcode;
    @NotBlank(message = "Enter a product name.") @Size(max = 100, message = "Use at most 100 characters.")
    private String name;
    @NotNull(message = "Enter a price.") @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99.")
    @Digits(integer = 6, fraction = 2, message = "Use at most six whole digits and two decimal places.")
    private BigDecimal price;
    @NotNull(message = "Enter stock quantity.") @Min(value = 0, message = "Stock cannot be negative.")
    @Max(value = 1000000, message = "Stock must not exceed 1000000.") private Integer stock;
    private boolean active = true;
    private Long version;
    public String getBarcode() { return barcode; } public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; } public void setStock(Integer stock) { this.stock = stock; }
    public boolean isActive() { return active; } public void setActive(boolean active) { this.active = active; }
    public Long getVersion() { return version; } public void setVersion(Long version) { this.version = version; }
}
