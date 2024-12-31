package com.tgd.order.service;

import com.tgd.order.clients.CustomerMsClient;
import com.tgd.order.clients.ProductCatalogMsClient;
import com.tgd.order.dto.*;
import com.tgd.order.entity.OrderItem;
import com.tgd.order.entity.Orders;
import com.tgd.order.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private CustomerMsClient customerMsClient;

    @Autowired
    private ProductCatalogMsClient pcmClient;


    public ResponseDTO<Orders> crateOrder(OrderDTO orderDTO) {
        ResponseDTO<Orders> responseDTO = new ResponseDTO<>();
        ResponseEntity<CustomerDTO> customerDTO = customerMsClient.getCustById(orderDTO.getCustomerId());
        log.debug("customer data : {}", customerDTO);

        if (!customerDTO.getStatusCode().is2xxSuccessful()) {
            responseDTO.setErrorCode(ErrorCode.FAILURE.getCode());
            responseDTO.setMessage("Customer data not found");
            return responseDTO;
        }

        boolean isAddressMatched = Objects.requireNonNull(customerDTO.getBody()).getAddresses().stream().anyMatch(address -> Objects.equals(address.getId(), orderDTO.getShipAddId()));
        if (!isAddressMatched) {
            responseDTO.setErrorCode(ErrorCode.FAILURE.getCode());
            responseDTO.setMessage("Customer address does not matched");
            return responseDTO;
        }

        List<Long> productIds = orderDTO.getOrderItems()
                .stream()
                .map(OrderItemDTO::getProductId)
                .toList();
        List<ProductDTO> products = pcmClient.getProductsByIds(productIds);
        log.debug("product data : {}", products);

        if (products != null && products.isEmpty()) {
            responseDTO.setErrorCode(ErrorCode.FAILURE.getCode());
            responseDTO.setMessage("Product data not found");
            return responseDTO;
        }
        assert products != null;
        boolean isAllMatched = products.stream().allMatch(prd -> productIds.contains(prd.getId()));
        if (!isAllMatched) {
            responseDTO.setErrorCode(ErrorCode.FAILURE.getCode());
            responseDTO.setMessage("Product data not matched with order data");
            return responseDTO;
        }

        BigDecimal totalAmt = calculateTotalPrice(products);
        Orders orders = new Orders();
        orders.setCustomerId(customerDTO.getBody().getId());
        orders.setShippingAddressId(orderDTO.getShipAddId());
        orders.setStatus("Pending");
        orders.setTotalAmount(totalAmt);
        orders.setPaymentStatus("Pending");
        orders.setOrderDate(LocalDateTime.now());
        orders.setUpdatedDate(LocalDateTime.now());

        List<OrderItem> orderItems = mapToOrderItems(orderDTO.getOrderItems(), products, orders);
        orders.setItems(orderItems);
        orders = ordersRepository.saveAndFlush(orders);
        responseDTO.setMessage(ErrorCode.SUCCESS.getMessage());
        responseDTO.setErrorCode(ErrorCode.SUCCESS.getCode());
        responseDTO.setData(orders);
        return responseDTO;
    }

    private BigDecimal calculateTotalPrice(List<ProductDTO> products) {
        return products.stream()
                .map(ProductDTO::getPrice) // Extract the price from each ProductDTO
                .filter(Objects::nonNull) // Ensure the price is not null
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all prices
    }

    public List<OrderItem> mapToOrderItems(List<OrderItemDTO> orderItemDTOs, List<ProductDTO> productDTOs, Orders orders) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : orderItemDTOs) {
            ProductDTO productDTO = findProductById(productDTOs, orderItemDTO.getProductId());
            if (productDTO != null) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(orderItemDTO.getProductId());
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItem.setPrice(productDTO.getPrice());
                orderItem.setTotalPrice(calculateTotalPrice(orderItemDTO.getQuantity(), productDTO.getPrice()));
                orderItem.setOrders(orders);
                orderItems.add(orderItem);
            }
        }

        return orderItems;
    }

    private ProductDTO findProductById(List<ProductDTO> productDTOs, Long productId) {
        return productDTOs.stream().filter(data -> Objects.equals(data.getId(), productId)).findFirst().orElse(null);
    }

    private BigDecimal calculateTotalPrice(int quantity, BigDecimal price) {
        if (price != null) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    public ResponseDTO<List<Orders>> byCustomerId(Long customerId) {
        ResponseDTO<List<Orders>> responseDTO = new ResponseDTO<>();
        List<Orders> ordersList = ordersRepository.findAllByCustomerId(customerId);
        if (ordersList == null || ordersList.isEmpty()) {
            responseDTO.setErrorCode(ErrorCode.FAILURE.getCode());
            responseDTO.setMessage("Customer data not found");
        } else {
            responseDTO.setErrorCode(ErrorCode.SUCCESS.getCode());
            responseDTO.setMessage(ErrorCode.SUCCESS.getMessage());
            responseDTO.setData(ordersList);
            return responseDTO;
        }
        return responseDTO;
    }

//    @Retry(name = "custms", fallbackMethod = "custMsFallBack")
    @CircuitBreaker(name = "custms", fallbackMethod = "custMsFallBack")
    public ResponseEntity<CustomerDTO> getCustomerData(Long customerId) throws IOException {
        log.info("retry {}", LocalDateTime.now());
//        throw new IOException("Retryb demo");
        return customerMsClient.getCustById(customerId);
    }

    public ResponseEntity<CustomerDTO> custMsFallBack(Throwable throwable) {
        log.debug("invoking fallback method");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
