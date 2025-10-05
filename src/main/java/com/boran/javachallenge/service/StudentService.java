package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.CartResponse;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.StudentRequest;
import com.boran.javachallenge.dto.StudentResponse;

import java.util.List;

public interface StudentService {

    // Customer/Student Servisleri
    StudentResponse addCustomer(StudentRequest request);
    List<CourseResponse> getCoursesForStudent(Long studentId);

    // Cart Servisleri
    CartResponse getCart(Long studentId);
    CartResponse addCourseToCart(Long studentId, Long courseId);
    CartResponse removeCourseFromCart(Long studentId, Long courseId);
    CartResponse emptyCart(Long studentId);
}
