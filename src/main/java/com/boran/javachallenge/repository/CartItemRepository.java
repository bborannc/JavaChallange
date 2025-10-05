package com.boran.javachallenge.repository;

import com.boran.javachallenge.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
