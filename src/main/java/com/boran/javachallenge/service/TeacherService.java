package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.CourseRequest;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.TeacherRequest;
import com.boran.javachallenge.dto.TeacherResponse;

import java.util.List;

public interface TeacherService {

    // Teacher Servisleri
    TeacherResponse createTeacher(TeacherRequest request);
    List<TeacherResponse> getTeachers();
    TeacherResponse updateTeacher(Long teacherId, TeacherRequest request);
    void deleteTeacher(Long teacherId);

    // Course Servisleri (Öğretmene Özel)
    List<CourseResponse> getAllCoursesForTeacher(Long teacherId);
    CourseResponse createCourseForTeacher(Long teacherId, CourseRequest request);
    CourseResponse updateCourseForTeacher(Long teacherId, Long courseId, CourseRequest request);
    void deleteCourseForTeacher(Long teacherId, Long courseId);
}
