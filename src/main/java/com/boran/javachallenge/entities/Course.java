package com.boran.javachallenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity {

    private String title;
    private String description;
    private BigDecimal price;
    private Integer capacity;
    private Integer enrolledStudentsCount = 0;
    private Double averageRating = 0.0;
    private Boolean isAvailable = true;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;



    @ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> students;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}

