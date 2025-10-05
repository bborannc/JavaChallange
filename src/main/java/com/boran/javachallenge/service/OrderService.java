package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    // Order Servisleri
    OrderResponse placeOrder(Long studentId);
    OrderResponse getOrderForCode(String orderCode);
    List<OrderResponse> getAllOrdersForCustomer(Long studentId);
}
