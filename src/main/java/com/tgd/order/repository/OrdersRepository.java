package com.tgd.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tgd.order.entity.Orders;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long>{

    List<Orders> findAllByCustomerId(Long customerId);
}
