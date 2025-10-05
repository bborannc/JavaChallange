package com.boran.javachallenge.dto;

import lombok.Data;
import java.util.Date;

@Data
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long cartId;
    private Date createdAt;
}
