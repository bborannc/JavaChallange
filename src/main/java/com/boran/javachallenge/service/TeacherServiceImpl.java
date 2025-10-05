package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.CourseRequest;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.TeacherRequest;
import com.boran.javachallenge.dto.TeacherResponse;
import com.boran.javachallenge.entities.Course;
import com.boran.javachallenge.entities.Teacher;
import com.boran.javachallenge.exception.ResourceNotFoundException;
import com.boran.javachallenge.repository.CourseRepository;
import com.boran.javachallenge.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    // Constructor Injection
    public TeacherServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    // --- TEACHER SERVİSLERİ ---

    @Override
    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstname());
        teacher.setLastName(request.getLastname());
        teacher.setEmail(request.getEmail());
        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapToTeacherResponse(savedTeacher);
    }

    @Override
    public List<TeacherResponse> getTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::mapToTeacherResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeacherResponse updateTeacher(Long teacherId, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğretmen", teacherId));

        teacher.setFirstName(request.getFirstname());
        teacher.setLastName(request.getLastname());
        teacher.setEmail(request.getEmail());
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return mapToTeacherResponse(updatedTeacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new ResourceNotFoundException("Öğretmen", teacherId);
        }
        teacherRepository.deleteById(teacherId);
    }

    // --- COURSE SERVİSLERİ (ÖĞRETMENE ÖZEL) ---

    @Override
    public List<CourseResponse> getAllCoursesForTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CourseResponse createCourseForTeacher(Long teacherId, CourseRequest request) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğretmen", teacherId));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());
        course.setTeacher(teacher);

        Course savedCourse = courseRepository.save(course);
        return mapToCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourseForTeacher(Long teacherId, Long courseId, CourseRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Kurs", courseId));

        // Kural: Kursun ilgili öğretmene ait olup olmadığını kontrol et
        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new ResourceNotFoundException("Kurs", courseId, "Bu kurs belirtilen öğretmene ait değil.");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setCapacity(request.getCapacity());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourseForTeacher(Long teacherId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Kurs", courseId));

        // Kural: Kursun ilgili öğretmene ait olup olmadığını kontrol et
        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new ResourceNotFoundException("Kurs", courseId, "Bu kurs belirtilen öğretmene ait değil.");
        }
        courseRepository.delete(course);
    }

    // --- MAPPER METOTLARI ---

    private TeacherResponse mapToTeacherResponse(Teacher teacher) {
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setFullName(teacher.getFirstName() + " " + teacher.getLastName());
        response.setEmail(teacher.getEmail());
        response.setCreatedAt(teacher.getCreatedAt());
        response.setCourseCount(teacher.getCourses() != null ? teacher.getCourses().size() : 0);
        return response;
    }

    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setPrice(course.getPrice());
        response.setCapacity(course.getCapacity());
        response.setEnrolledStudentsCount(course.getEnrolledStudentsCount());
        response.setAverageRating(course.getAverageRating());
        response.setIsAvailable(course.getIsAvailable());
        if (course.getTeacher() != null) {
            response.setTeacherId(course.getTeacher().getId());
            response.setTeacherName(course.getTeacher().getFirstName() + " " + course.getTeacher().getLastName());
        }
        return response;
    }
}
