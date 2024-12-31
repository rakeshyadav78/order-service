package com.tgd.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tgd.order.entity.OrderStatus;
import com.tgd.order.repository.OrderStatusRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderStatusService {

	
	@Autowired
	private OrderStatusRepository orderStatusRepository;
	
	public OrderStatus getById(Long id) {
		return orderStatusRepository.findById(id).orElse(null);
	}
}
