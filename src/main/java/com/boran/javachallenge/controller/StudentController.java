package com.boran.javachallenge.controller;

import com.boran.javachallenge.dto.CartResponse;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.StudentRequest;
import com.boran.javachallenge.dto.StudentResponse;
import com.boran.javachallenge.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ----------------------------------------------------------------------
    // STUDENT SERVİSLERİ
    // ----------------------------------------------------------------------

    // POST /api/students : AddCustomer
    @PostMapping
    public ResponseEntity<StudentResponse> addCustomer(@RequestBody StudentRequest request) {
        StudentResponse response = studentService.addCustomer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/students/{studentId}/enrolled-courses : GetCoursesForStudent
    @GetMapping("/{studentId}/enrolled-courses")
    public ResponseEntity<List<CourseResponse>> getCoursesForStudent(@PathVariable Long studentId) {
        List<CourseResponse> courses = studentService.getCoursesForStudent(studentId);
        return ResponseEntity.ok(courses);
    }




    @GetMapping("/{studentId}/cart")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long studentId) {
        CartResponse cart = studentService.getCart(studentId);
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/{studentId}/cart/add-course/{courseId}")
    public ResponseEntity<CartResponse> addCourseToCart(@PathVariable Long studentId,
                                                        @PathVariable Long courseId) {
        CartResponse cart = studentService.addCourseToCart(studentId, courseId);
        return ResponseEntity.ok(cart);
    }


    @DeleteMapping("/{studentId}/cart/remove-course/{courseId}")
    public ResponseEntity<CartResponse> removeCourseFromCart(@PathVariable Long studentId,
                                                             @PathVariable Long courseId) {
        CartResponse cart = studentService.removeCourseFromCart(studentId, courseId);
        return ResponseEntity.ok(cart);
    }


    @DeleteMapping("/{studentId}/cart/empty")
    public ResponseEntity<CartResponse> emptyCart(@PathVariable Long studentId) {
        CartResponse cart = studentService.emptyCart(studentId);
        return ResponseEntity.ok(cart);
    }
}
