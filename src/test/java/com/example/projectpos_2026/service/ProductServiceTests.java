package com.example.projectpos_2026.service;

import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest @Transactional
class ProductServiceTests {
    @Autowired ProductService products;
    String barcode() { return UUID.randomUUID().toString(); }
    @Test void createsAndUpdatesInventory() {
        Product p = products.create(barcode(), "Rice", new BigDecimal("8.50"), 20);
        products.update(p.getId(), p.getVersion(), p.getBarcode(), "Rice 1kg", new BigDecimal("9.00"), 25, false);
        assertThat(products.get(p.getId()).getStock()).isEqualTo(25);
        assertThat(p.isActive()).isFalse();
    }
    @Test void rejectsDuplicateBarcode() {
        String code = barcode(); products.create(code, "Milk", BigDecimal.ONE, 5);
        assertThatThrownBy(() -> products.create(code, "Other milk", BigDecimal.ONE, 5))
                .isInstanceOf(BusinessException.class).hasMessageContaining("already in use");
    }
    @Test void rejectsInvalidPriceAndStock() {
        assertThatThrownBy(() -> products.create(barcode(), "Milk", new BigDecimal("1.001"), 5)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> products.create(barcode(), "Milk", BigDecimal.ONE, -1)).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> products.create(barcode(), "Milk", BigDecimal.ZERO, 5)).isInstanceOf(BusinessException.class);
    }
    @Test void preventsStaleInventoryEdits() {
        Product p = products.create(barcode(), "Rice", BigDecimal.ONE, 10);
        Long version = p.getVersion();
        products.update(p.getId(), version, p.getBarcode(), p.getName(), p.getPrice(), 8, true);
        assertThatThrownBy(() -> products.update(p.getId(), version, p.getBarcode(), p.getName(), p.getPrice(), 10, true))
                .isInstanceOf(BusinessException.class).hasMessageContaining("changed");
    }
}
