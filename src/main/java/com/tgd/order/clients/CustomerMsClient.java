package com.tgd.order.clients;

import com.tgd.order.util.LoadBalancerConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tgd.order.dto.CustomerDTO;
import com.tgd.order.dto.OrderDTO;

@LoadBalancerClient(name = "customer-ms",configuration = LoadBalancerConfiguration.class)
@FeignClient(value = "CUSTOMER-MS")
public interface CustomerMsClient {

	@LoadBalanced
	@GetMapping(value = "/v1/customers/{custId}")
	ResponseEntity<CustomerDTO> getCustById(@PathVariable(value = "custId") Long custId);
}
