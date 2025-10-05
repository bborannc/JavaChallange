package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.CartResponse;
import com.boran.javachallenge.dto.CourseResponse;
import com.boran.javachallenge.dto.StudentRequest;
import com.boran.javachallenge.dto.StudentResponse;
import com.boran.javachallenge.entities.Cart;
import com.boran.javachallenge.entities.CartItem;
import com.boran.javachallenge.entities.Course;
import com.boran.javachallenge.entities.Student;
import com.boran.javachallenge.exception.BusinessRuleException;
import com.boran.javachallenge.exception.ResourceNotFoundException;
import com.boran.javachallenge.repository.CartItemRepository;
import com.boran.javachallenge.repository.CartRepository;
import com.boran.javachallenge.repository.CourseRepository;
import com.boran.javachallenge.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              CartRepository cartRepository,
                              CartItemRepository cartItemRepository,
                              CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.courseRepository = courseRepository;
    }

    // --- STUDENT SERVİSLERİ ---

    @Override
    @Transactional
    public StudentResponse addCustomer(StudentRequest request) {
        Optional<Student> existingStudent = studentRepository.findByEmail((request.getEmail()));
        if (existingStudent.isPresent()) {
            throw new BusinessRuleException("Bu e-posta adresiyle kayıtlı bir müşteri zaten mevcut.");
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());

        // Kural: Yeni müşteri oluşturulurken ona sepet oluşturulur
        Cart cart = new Cart();
        cart.setStudent(student);
        cart.setItems(new ArrayList<>());

        student.setCart(cart);
        Student savedStudent = studentRepository.save(student);
        cartRepository.save(cart);

        return mapToStudentResponse(savedStudent);
    }

    @Override
    public List<CourseResponse> getCoursesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Öğrenci", studentId));

        // Basit DTO dönüşümü (Normalde bir CourseMapper kullanılır)
        return student.getEnrolledCourses().stream()
                .map(c -> {
                    CourseResponse cr = new CourseResponse();
                    cr.setId(c.getId());
                    cr.setTitle(c.getTitle());
                    cr.setPrice(c.getPrice());
                    cr.setAverageRating(c.getAverageRating());
                    return cr;
                })
                .collect(Collectors.toList());
    }

    // --- CART SERVİSLERİ ---

    @Override
    public CartResponse getCart(Long studentId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        if (cart == null) {
            throw new ResourceNotFoundException("Sepet", "Öğrenci ID: " + studentId);
        }
        return mapToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addCourseToCart(Long studentId, Long courseId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Kurs", courseId));

        // Kural 1: Sepete sadece eklenebilir durumda olan kursları ekle
        if (course.getIsAvailable() == null || !course.getIsAvailable()) {
            throw new BusinessRuleException("Kurs (" + course.getTitle() + ") sepete eklenmeye uygun değil.");
        }

        // Kural 2: Aynı kursun sepette tekrar olup olmadığını kontrol et
        boolean alreadyInCart = cart.getItems().stream()
                .anyMatch(item -> item.getCourse().getId().equals(courseId));

        if (alreadyInCart) {
            throw new BusinessRuleException("Kurs zaten sepetinizde bulunuyor.");
        }

        // Sepet kalemi oluştur
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setCourse(course);
        cartItem.setPrice(course.getPrice()); // Fiyatı anlık olarak kaydet

        cart.getItems().add(cartItem);
        cartItemRepository.save(cartItem);

        // Kural 3: Sepetin toplam fiyatı her işlemde hesaplansın
        updateCartTotalPrice(cart);

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse removeCourseFromCart(Long studentId, Long courseId) {
        Cart cart = cartRepository.findByStudentId(studentId);

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getCourse().getId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Sepet Kalemi", + courseId, "Bu kurs sepette bulunamadı."));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);

        // Kural: Sepetin toplam fiyatı her işlemde hesaplansın
        updateCartTotalPrice(cart);

        return mapToCartResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse emptyCart(Long studentId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        if (cart == null) {
            throw new ResourceNotFoundException("Sepet", "Öğrenci ID: " + studentId);
        }

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

        // Kural: Toplam fiyatı sıfırla
        updateCartTotalPrice(cart);

        return mapToCartResponse(cartRepository.save(cart));
    }

    // --- HELPER VE MAPPER METOTLARI ---

    private void updateCartTotalPrice(Cart cart) {
        BigDecimal newTotalPrice = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(newTotalPrice);
    }

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setCartId(student.getCart() != null ? student.getCart().getId() : null);
        return response;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setStudentId(cart.getStudent().getId());
        response.setTotalPrice(cart.getTotalPrice());
        // CartResponse.CartItemDTO'nun var olduğu varsayılır
        response.setItems(cart.getItems().stream()
                .map(item -> new CartResponse.CartItemDTO(
                        item.getId(),
                        item.getCourse().getId(),
                        item.getCourse().getTitle(),
                        item.getPrice()))
                .collect(Collectors.toList()));
        return response;
    }
}
