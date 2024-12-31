package com.tgd.order.controller;

import com.tgd.order.dto.CustomerDTO;
import com.tgd.order.dto.ResponseDTO;
import com.tgd.order.service.OrdersService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tgd.order.dto.OrderDTO;
import com.tgd.order.entity.Orders;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/order")
public class OrderController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private Environment environment;


	@PostMapping
	public ResponseEntity<ResponseDTO<?>> createOrder(@RequestBody OrderDTO orderDTO) {
		log.debug("order request : {}",orderDTO);
		ResponseDTO<Orders> responseDTO= ordersService.crateOrder(orderDTO);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/customerId/{customerId}")
	public ResponseEntity<ResponseDTO<List<Orders>>> createOrder(@PathVariable(value = "customerId") Long customerId) {
		log.debug("fetching order by customerId : {}",customerId);
		ResponseDTO<List<Orders>> responseDTO= ordersService.byCustomerId(customerId);
		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/custData/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerData(@PathVariable(value = "customerId") Long customerId) throws IOException {
		log.debug("fetching by customerId : {}",customerId);

		return ordersService.getCustomerData(customerId);
//		return ResponseEntity.ok(responseDTO);
	}

	@GetMapping("/getServiceInstance")
	public List<ServiceInstance> getAllServiceInstance(){
		String appName=environment.getProperty("spring.application.name");
		return  discoveryClient.getInstances(appName);
	}
}
