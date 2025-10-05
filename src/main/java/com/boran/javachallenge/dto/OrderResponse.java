package com.boran.javachallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long studentId;
    private BigDecimal totalPrice;
    private String status;
    private Date createdAt;
    private List<OrderItemDTO> items;


    @Data
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private Long courseId;
        private String courseTitle;
        private BigDecimal price;
    }
}
