package com.ecom.cart_service.feign;

import com.ecom.cart_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 1. Get by Product ID
@FeignClient(name = "product-service", url = "${feign.client.product-service.url}") //  The name should match the product service's application name
public interface ProductFeignProvider {

    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id);

    @GetMapping("/api/products/ping")
    void ping();
}
