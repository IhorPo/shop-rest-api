package com.shoprestapi.dto;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private List<Long> products;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }
}
