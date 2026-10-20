package com.example.projectpos_2026.service;

import com.example.projectpos_2026.entity.*;
import com.example.projectpos_2026.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.IntSupplier;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ConcurrencyTests {
    @Autowired ProductService products; @Autowired SaleService sales; @Autowired ReturnService returns;
    String key() { return UUID.randomUUID().toString(); }
    int successes(IntSupplier action) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2); CountDownLatch start = new CountDownLatch(1);
        Callable<Integer> operation = () -> { start.await(); try { return action.getAsInt(); } catch (BusinessException ex) { return 0; } };
        try {
            Future<Integer> a = pool.submit(operation), b = pool.submit(operation); start.countDown();
            return a.get(20, TimeUnit.SECONDS) + b.get(20, TimeUnit.SECONDS);
        } finally { pool.shutdownNow(); }
    }
    @Test void concurrentSalesCannotOversellLastUnit() throws Exception {
        Product p = products.create(key(), "Last unit", BigDecimal.ONE, 1);
        assertThat(successes(() -> { sales.checkout(Map.of(p.getId(), 1), BigDecimal.TEN, key()); return 1; })).isEqualTo(1);
        assertThat(products.get(p.getId()).getStock()).isZero();
    }
    @Test void concurrentReturnsCannotRefundSameUnitTwice() throws Exception {
        Product p = products.create(key(), "Single returned unit", BigDecimal.ONE, 1);
        Sale sale = sales.checkout(Map.of(p.getId(), 1), BigDecimal.TEN, key()); Long item = sale.getItems().get(0).getId();
        assertThat(successes(() -> { returns.process(sale.getId(), item, 1, "Wrong item", key()); return 1; })).isEqualTo(1);
        assertThat(products.get(p.getId()).getStock()).isEqualTo(1);
        assertThat(returns.forSale(sale.getId())).hasSize(1);
    }
}
