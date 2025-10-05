package com.boran.javachallenge.service;

import com.boran.javachallenge.dto.OrderResponse;
import com.boran.javachallenge.entities.*;
import com.boran.javachallenge.exception.BusinessRuleException;
import com.boran.javachallenge.exception.ResourceNotFoundException;
import com.boran.javachallenge.repository.CartRepository;
import com.boran.javachallenge.repository.CourseRepository;
import com.boran.javachallenge.repository.OrderRepository;
import com.boran.javachallenge.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            CourseRepository courseRepository,
                            StudentRepository studentRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    // --- ORDER SERVİSLERİ ---

    @Override
    @Transactional
    public OrderResponse placeOrder(Long studentId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Sepet boş olduğu için sipariş oluşturulamaz.");
        }
        Student student = cart.getStudent();

        // 1. İş Kuralı Kontrolü (Kapasite ve Mevcut Kayıt)
        for (CartItem item : cart.getItems()) {
            Course course = item.getCourse();

            // Kural: Kurs kapasitesi kontrolü
            if (course.getEnrolledStudentsCount() >= course.getCapacity()) {
                throw new BusinessRuleException("Kurs (" + course.getTitle() + ") kapasitesi dolmuştur. Sipariş iptal edildi.");
            }

            // Kural: Öğrencinin zaten kayıtlı olup olmadığı kontrolü
            if (student.getEnrolledCourses().contains(course)) {
                throw new BusinessRuleException("Öğrenci zaten " + course.getTitle() + " kursuna kayıtlı. Sipariş iptal edildi.");
            }
        }

        // 2. Sipariş (Order) Oluşturma
        Order order = new Order();
        order.setStudent(student);
        order.setOrderCode(UUID.randomUUID().toString());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("COMPLETED");

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setCourse(cartItem.getCourse());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 3. Kurslara Kayıt ve Kapasite Güncelleme
        for (CartItem item : cart.getItems()) {
            Course course = item.getCourse();

            // Öğrenciyi kursa kaydet (Many-to-Many)
            student.getEnrolledCourses().add(course);

            // Kursun öğrenci sayısını artır
            course.setEnrolledStudentsCount(course.getEnrolledStudentsCount() + 1);
            courseRepository.save(course);
        }
        studentRepository.save(student); // Öğrencinin güncellenmiş kurs listesini kaydet

        // 4. Sepeti Boşaltma
        cart.getItems().clear();
        cart.setTotalPrice(order.getTotalPrice()); // Sepetin toplamını sıfırla (veya son sipariş fiyatına ayarla)
        cartRepository.save(cart);

        return mapToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderForCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Sipariş", "Kodu: " + orderCode));

        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrdersForCustomer(Long studentId) {
        return orderRepository.findByStudentId(studentId).stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    // --- MAPPER METOTLARI ---

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderCode(order.getOrderCode());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setStudentId(order.getStudent().getId());
        response.setCreatedAt(order.getCreatedAt());
        // OrderResponse.OrderItemDTO'nun var olduğu varsayılır
        response.setItems(order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemDTO(
                        item.getId(),
                        item.getCourse().getId(),
                        item.getCourse().getTitle(),
                        item.getPrice()))
                .collect(Collectors.toList()));
        return response;
    }
}
