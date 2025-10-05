package com.boran.javachallenge.repository;

import com.boran.javachallenge.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByStudentId(Long studentId);
}
