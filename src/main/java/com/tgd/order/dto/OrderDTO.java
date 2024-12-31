package com.tgd.order.dto;

import java.util.List;

import lombok.Data;


@Data
public class OrderDTO {
	private Long customerId; // Unique identifier for the customer
	private Long shipAddId; // Shipping address ID
	private List<OrderItemDTO> orderItems; // List of order items
}
