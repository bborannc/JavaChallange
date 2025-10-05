package com.boran.javachallenge.controller;

import com.boran.javachallenge.dto.CourseRequest;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.TeacherRequest;
import com.boran.javachallenge.dto.TeacherResponse;
import com.boran.javachallenge.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;


    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }




    @PostMapping
    public ResponseEntity<TeacherResponse> createTeacher(@RequestBody TeacherRequest request) {
        TeacherResponse response = teacherService.createTeacher(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getTeachers() {
        List<TeacherResponse> teachers = teacherService.getTeachers();
        return ResponseEntity.ok(teachers);
    }


    @PutMapping("/{teacherId}")
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable Long teacherId,
                                                         @RequestBody TeacherRequest request) {
        TeacherResponse response = teacherService.updateTeacher(teacherId, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.noContent().build();
    }




    @GetMapping("/{teacherId}/courses")
    public ResponseEntity<List<CourseResponse>> getAllCoursesForTeacher(@PathVariable Long teacherId) {
        List<CourseResponse> courses = teacherService.getAllCoursesForTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }


    @PostMapping("/{teacherId}/courses")
    public ResponseEntity<CourseResponse> createCourseForTeacher(@PathVariable Long teacherId,
                                                                 @RequestBody CourseRequest request) {
        CourseResponse response = teacherService.createCourseForTeacher(teacherId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{teacherId}/courses/{courseId}")
    public ResponseEntity<CourseResponse> updateCourseForTeacher(@PathVariable Long teacherId,
                                                                 @PathVariable Long courseId,
                                                                 @RequestBody CourseRequest request) {
        CourseResponse response = teacherService.updateCourseForTeacher(teacherId, courseId, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{teacherId}/courses/{courseId}")
    public ResponseEntity<Void> deleteCourseForTeacher(@PathVariable Long teacherId,
                                                       @PathVariable Long courseId) {
        teacherService.deleteCourseForTeacher(teacherId, courseId);
        return ResponseEntity.noContent().build();
    }
}
