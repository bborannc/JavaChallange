package com.boran.javachallenge.controller;

import com.boran.javachallenge.dto.OrderResponse;
import com.boran.javachallenge.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ----------------------------------------------------------------------
    // ORDER SERVİSLERİ
    // ----------------------------------------------------------------------

    // POST /api/orders/place/{studentId} : PlaceOrder
    @PostMapping("/place/{studentId}")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable Long studentId) {
        OrderResponse order = orderService.placeOrder(studentId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // GET /api/orders/code/{orderCode} : GetOrderForCode
    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderResponse> getOrderForCode(@PathVariable String orderCode) {
        OrderResponse order = orderService.getOrderForCode(orderCode);
        return ResponseEntity.ok(order);
    }

    // GET /api/orders/customer/{studentId} : GetAllOrdersForCustomer
    @GetMapping("/customer/{studentId}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForCustomer(@PathVariable Long studentId) {
        List<OrderResponse> orders = orderService.getAllOrdersForCustomer(studentId);
        return ResponseEntity.ok(orders);
    }
}