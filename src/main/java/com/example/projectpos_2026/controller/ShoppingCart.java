package com.example.projectpos_2026.controller;

import com.example.projectpos_2026.exception.BusinessException;
import java.io.Serializable;
import java.util.*;

public class ShoppingCart implements Serializable {
    private final Map<Long, Integer> quantities = new TreeMap<>();
    private String checkoutKey = UUID.randomUUID().toString();
    public synchronized void add(Long id, int quantity, int available) {
        long updated = (long) quantities.getOrDefault(id, 0) + quantity;
        if (quantity < 1 || updated > 10000) throw new BusinessException("Quantity must be between 1 and 10000.");
        if (updated > available) throw new BusinessException("Insufficient stock for this quantity.");
        if (!quantities.containsKey(id) && quantities.size() >= 100) throw new BusinessException("A sale can contain at most 100 products.");
        quantities.put(id, (int) updated); checkoutKey = UUID.randomUUID().toString();
    }
    public synchronized void remove(Long id) { quantities.remove(id); checkoutKey = UUID.randomUUID().toString(); }
    public synchronized void clear() { quantities.clear(); checkoutKey = UUID.randomUUID().toString(); }
    public synchronized Map<Long, Integer> items() { return new TreeMap<>(quantities); }
    public synchronized String getCheckoutKey() { return checkoutKey; }
}
