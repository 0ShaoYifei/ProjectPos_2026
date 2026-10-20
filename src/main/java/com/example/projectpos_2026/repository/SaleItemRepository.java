package com.example.projectpos_2026.repository;

import com.example.projectpos_2026.entity.SaleItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from SaleItem i where i.id = :id and i.sale.id = :saleId")
    Optional<SaleItem> findLocked(@Param("id") Long id, @Param("saleId") Long saleId);
}
