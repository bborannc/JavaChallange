package com.boran.javachallenge.repository;

import com.boran.javachallenge.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByStudentId(Long studentId);

}
