package com.boran.javachallenge.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Teacher extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;


    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses;
}
