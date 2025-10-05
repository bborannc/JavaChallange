package com.boran.javachallenge.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherResponse {
    private Long id;
    private String fullName;
    private String email;
    private Date createdAt;
    private int courseCount;
}
