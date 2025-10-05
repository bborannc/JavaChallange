package com.boran.javachallenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private Long studentId;
    private BigDecimal totalPrice;
    private List<CartItemDTO> items;


    @Data
    @AllArgsConstructor
    public static class CartItemDTO {
        private Long id;
        private Long courseId;
        private String courseTitle;
        private BigDecimal price;
    }
}
