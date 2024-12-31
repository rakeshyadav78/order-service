package com.tgd.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_status")
public class OrderStatus {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",nullable = false, unique = true)
	private Long id;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "description",nullable = false)
    private String description;

}
