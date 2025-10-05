package com.boran.javachallenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cart extends BaseEntity {


    private BigDecimal totalPrice = BigDecimal.ZERO;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<CartItem> items;


    public void calculateTotalPrice() {
        this.totalPrice = items.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
