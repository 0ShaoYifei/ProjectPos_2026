package com.example.projectpos_2026.repository;

import com.example.projectpos_2026.entity.Sale;
import org.springframework.data.jpa.repository.*;
import java.util.*;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @EntityGraph(attributePaths = "items") Optional<Sale> findDetailedById(Long id);
    @EntityGraph(attributePaths = "items") Optional<Sale> findByCheckoutKey(String checkoutKey);
    List<Sale> findAllByOrderByCreatedAtDesc();
}
