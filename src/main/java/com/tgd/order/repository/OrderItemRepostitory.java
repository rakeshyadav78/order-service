package com.tgd.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tgd.order.entity.OrderItem;

public interface OrderItemRepostitory extends JpaRepository<OrderItem, Long>{

}
