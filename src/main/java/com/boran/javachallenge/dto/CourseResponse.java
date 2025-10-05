package com.boran.javachallenge.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer capacity;
    private Integer enrolledStudentsCount;
    private Double averageRating;
    private Boolean isAvailable;
    private Long teacherId;
    private String teacherName;
}

