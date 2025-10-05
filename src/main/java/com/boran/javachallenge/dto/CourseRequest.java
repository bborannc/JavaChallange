package com.boran.javachallenge.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Integer capacity;
}
