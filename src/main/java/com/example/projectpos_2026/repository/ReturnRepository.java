package com.example.projectpos_2026.repository;

import com.example.projectpos_2026.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReturnRepository extends JpaRepository<ReturnRecord, Long> {
    Optional<ReturnRecord> findByRequestKey(String requestKey);
    List<ReturnRecord> findBySaleItemSaleIdOrderByCreatedAtDesc(Long saleId);
}
