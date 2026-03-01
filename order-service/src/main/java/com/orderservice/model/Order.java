package com.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String productName;
    private Long price;
    private String userId;
    private String productId;
    private Integer quantity;
    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
