package com.tgd.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tgd.order.entity.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long>{

}
