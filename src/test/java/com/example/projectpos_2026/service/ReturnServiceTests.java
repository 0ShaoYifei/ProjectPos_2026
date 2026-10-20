package com.example.projectpos_2026.service;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import com.example.projectpos_2026.repository.ReturnRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReturnServiceTests {
    @Autowired ProductService products; @Autowired SaleService sales;
    @Autowired ReturnService returns; @Autowired ReturnRepository repository;
    String key() { return UUID.randomUUID().toString(); }
    Sale sale(int quantity) {
        Product p = products.create(key(), "Return test product", new BigDecimal("2.35"), 10);
        return sales.checkout(Map.of(p.getId(), quantity), BigDecimal.TEN, key());
    }
    @Test void refundsOriginalPriceAndRestoresInventory() {
        Sale sale = sale(3); SaleItem item = sale.getItems().get(0); Long productId = item.getProduct().getId();
        Product p = products.get(productId);
        products.update(p.getId(), p.getVersion(), p.getBarcode(), "New name", new BigDecimal("9.00"), p.getStock(), false);
        ReturnRecord result = returns.process(sale.getId(), item.getId(), 2, "Incorrect purchase", key());
        assertThat(result.getRefund()).isEqualByComparingTo("4.70");
        assertThat(products.get(productId).getStock()).isEqualTo(9);
        assertThat(sales.get(sale.getId()).getItems().get(0).getReturnableQuantity()).isEqualTo(1);
        assertThat(returns.forSale(sale.getId())).hasSize(1);
    }
    @Test void preventsExcessiveAndCumulativeOverReturns() {
        Sale sale = sale(3); SaleItem item = sale.getItems().get(0); long before = repository.count();
        returns.process(sale.getId(), item.getId(), 2, "Wrong item", key());
        assertThatThrownBy(() -> returns.process(sale.getId(), item.getId(), 2, "Wrong item", key()))
                .isInstanceOf(BusinessException.class).hasMessageContaining("exceeds");
        assertThat(repository.count()).isEqualTo(before + 1);
        assertThat(products.get(item.getProduct().getId()).getStock()).isEqualTo(9);
        returns.process(sale.getId(), item.getId(), 1, "Remaining item", key());
        assertThat(sales.get(sale.getId()).getRefundedTotal()).isEqualByComparingTo("7.05");
        assertThatThrownBy(() -> returns.process(sale.getId(), item.getId(), 1, "Again", key())).isInstanceOf(BusinessException.class);
    }
    @Test void rejectsInvalidQuantityReasonAndReceiptItemMismatch() {
        Sale sale = sale(1); SaleItem item = sale.getItems().get(0);
        assertThatThrownBy(() -> returns.process(sale.getId(), item.getId(), 0, "Wrong item", key())).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> returns.process(sale.getId(), item.getId(), 1, " ", key())).isInstanceOf(BusinessException.class);
        assertThatThrownBy(() -> returns.process(Long.MAX_VALUE, item.getId(), 1, "Wrong item", key())).isInstanceOf(BusinessException.class).hasMessageContaining("not found");
        assertThat(sales.get(sale.getId()).getItems().get(0).getReturnedQuantity()).isZero();
    }
    @Test void repeatedRequestDoesNotDoubleRefundOrRestock() {
        Sale sale = sale(2); SaleItem item = sale.getItems().get(0); String token = key(); long before = repository.count();
        ReturnRecord first = returns.process(sale.getId(), item.getId(), 1, "Wrong item", token);
        ReturnRecord repeated = returns.process(sale.getId(), item.getId(), 1, "Wrong item", token);
        assertThat(repeated.getId()).isEqualTo(first.getId());
        assertThat(repository.count()).isEqualTo(before + 1);
        assertThat(products.get(item.getProduct().getId()).getStock()).isEqualTo(9);
    }
    @Test void rejectsExpiredFormToken() {
        Sale sale = sale(1);
        assertThatThrownBy(() -> returns.process(sale.getId(), sale.getItems().get(0).getId(), 1, "Wrong item", "invalid"))
                .isInstanceOf(BusinessException.class).hasMessageContaining("expired");
    }
    @Test void inventoryOverflowRollsBackReturnRecordAndReturnedQuantity() {
        Sale sale = sale(1); SaleItem item = sale.getItems().get(0); Product p = products.get(item.getProduct().getId());
        products.update(p.getId(), p.getVersion(), p.getBarcode(), p.getName(), p.getPrice(), 1000000, true);
        long before = repository.count();
        assertThatThrownBy(() -> returns.process(sale.getId(), item.getId(), 1, "Wrong item", key())).isInstanceOf(BusinessException.class).hasMessageContaining("inventory limit");
        assertThat(repository.count()).isEqualTo(before);
        assertThat(sales.get(sale.getId()).getItems().get(0).getReturnedQuantity()).isZero();
    }
}
