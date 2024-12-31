package com.tgd.order.dto;

import lombok.Data;

@Data
public class InventoryDTO {
	private Long id;
	private Long productId;
	private Integer stockQuantity;
	private Integer reservedQuantity;
}
