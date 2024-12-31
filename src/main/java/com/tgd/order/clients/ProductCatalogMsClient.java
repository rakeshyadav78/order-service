package com.tgd.order.clients;

import com.tgd.order.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "PRODUCT-CATALOG")
public interface ProductCatalogMsClient {

    @PostMapping("/v1/products/getProductsByIds")
    List<ProductDTO> getProductsByIds(@RequestBody List<Long> ids);
}
