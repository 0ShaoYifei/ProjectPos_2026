package com.example.projectpos_2026.service;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SaleServiceTests {
    @Autowired ProductService products;
    @Autowired SaleService sales;
    @Autowired SaleRepository repository;
    String key() { return UUID.randomUUID().toString(); }
    Product product(String price, int stock) { return products.create(key(), "Test product", new BigDecimal(price), stock); }
    @Test void recordsMultipleItemsCashChangeAndInventory() {
        Product a = product("2.35", 10), b = product("1.10", 8);
        Sale sale = sales.checkout(Map.of(a.getId(), 3, b.getId(), 2), new BigDecimal("10.00"), key());
        assertThat(sale.getTotal()).isEqualByComparingTo("9.25");
        assertThat(sale.getChangeDue()).isEqualByComparingTo("0.75");
        assertThat(sales.get(sale.getId()).getItems()).hasSize(2);
        assertThat(products.get(a.getId()).getStock()).isEqualTo(7);
        assertThat(products.get(b.getId()).getStock()).isEqualTo(6);
    }
    @Test void acceptsExactPaymentWithoutRoundingErrors() {
        Product p = product("0.10", 3);
        Sale s = sales.checkout(Map.of(p.getId(), 3), new BigDecimal("0.30"), key());
        assertThat(s.getTotal()).isEqualByComparingTo("0.30");
        assertThat(s.getChangeDue()).isEqualByComparingTo("0.00");
    }
    @Test void rejectsInsufficientStockWithoutPersistingSaleOrDecreasingOtherStock() {
        Product a = product("1.00", 5), b = product("2.00", 0); long before = repository.count();
        assertThatThrownBy(() -> sales.checkout(Map.of(a.getId(), 1, b.getId(), 1), new BigDecimal("20"), key()))
                .isInstanceOf(BusinessException.class).hasMessageContaining("Insufficient stock");
        assertThat(repository.count()).isEqualTo(before);
        assertThat(products.get(a.getId()).getStock()).isEqualTo(5);
    }
    @Test void rejectsInsufficientCashWithoutStockOrSaleChanges() {
        Product p = product("3.25", 5); long before = repository.count();
        assertThatThrownBy(() -> sales.checkout(Map.of(p.getId(), 2), new BigDecimal("6.49"), key()))
                .isInstanceOf(BusinessException.class).hasMessageContaining("Insufficient payment");
        assertThat(products.get(p.getId()).getStock()).isEqualTo(5);
        assertThat(repository.count()).isEqualTo(before);
    }
    @Test void rejectsEmptyCartInvalidQuantityAndFractionalCash() {
        Product p = product("1.00", 5);
        assertThatThrownBy(() -> sales.checkout(Map.of(), BigDecimal.TEN, key())).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> sales.checkout(Map.of(p.getId(), 0), BigDecimal.TEN, key())).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> sales.checkout(Map.of(p.getId(), 1), new BigDecimal("1.001"), key())).isInstanceOf(BusinessException.class);
    }
    @Test void rejectsInactiveAndMissingProducts() {
        Product p = product("1.00", 5);
        products.update(p.getId(), p.getVersion(), p.getBarcode(), p.getName(), p.getPrice(), p.getStock(), false);
        assertThatThrownBy(() -> sales.checkout(Map.of(p.getId(), 1), BigDecimal.TEN, key())).isInstanceOf(BusinessException.class).hasMessageContaining("inactive");
        assertThatThrownBy(() -> sales.checkout(Map.of(Long.MAX_VALUE, 1), BigDecimal.TEN, key())).isInstanceOf(BusinessException.class).hasMessageContaining("not found");
    }
    @Test void checkoutKeyPreventsDuplicateTransactions() {
        Product p = product("1.00", 5); String token = key(); long before = repository.count();
        Sale first = sales.checkout(Map.of(p.getId(), 2), BigDecimal.TEN, token);
        Sale second = sales.checkout(Map.of(p.getId(), 2), BigDecimal.TEN, token);
        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(repository.count()).isEqualTo(before + 1);
        assertThat(products.get(p.getId()).getStock()).isEqualTo(3);
    }
    @Test void preservesReceiptPriceAfterProductPriceChanges() {
        Product p = product("2.50", 5);
        Sale sale = sales.checkout(Map.of(p.getId(), 2), BigDecimal.TEN, key());
        Product latest = products.get(p.getId());
        products.update(latest.getId(), latest.getVersion(), latest.getBarcode(), "Renamed product", new BigDecimal("9.99"), latest.getStock(), true);
        SaleItem item = sales.get(sale.getId()).getItems().get(0);
        assertThat(item.getUnitPrice()).isEqualByComparingTo("2.50");
        assertThat(item.getProductName()).isEqualTo("Test product");
    }
}
