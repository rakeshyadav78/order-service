package com.tgd.order.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Column(name = "shipping_address_id", nullable = false)
	private Long shippingAddressId;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Column(name = "payment_status", nullable = false)
	private String paymentStatus;

	@Column(name = "order_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime orderDate;

	@Column(name = "updated_date", columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedDate;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orders")
	@JsonManagedReference
	private List<OrderItem> items;
}
