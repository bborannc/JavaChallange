package com.boran.javachallenge.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;


    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Cart cart;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Order> orders;


    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses;
}
