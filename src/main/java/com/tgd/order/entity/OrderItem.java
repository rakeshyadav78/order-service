package com.tgd.order.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false, unique = true)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderid", nullable = false)
	@JsonBackReference
    private Orders orders;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price",nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;


}
