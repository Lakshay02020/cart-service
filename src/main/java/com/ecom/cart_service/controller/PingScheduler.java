package com.ecom.cart_service.controller;

import com.ecom.cart_service.feign.ProductFeignProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RestController
@RequestMapping("/admin")
@Slf4j
public class PingScheduler {

    // TODO understand volatile meaning here
    private volatile boolean isPingJobEnabled = true;

    private final ProductFeignProvider productFeignProvider;

    public PingScheduler(ProductFeignProvider productFeignProvider) {
        this.productFeignProvider = productFeignProvider;
    }

    @PostMapping("/ping-toggle")
    public void togglePing(@RequestParam boolean enable) {
        this.isPingJobEnabled = enable;
        log.info("Ping job toggled to: {}", enable);
    }

    @Scheduled(fixedRate = 7 * 60 * 1000)
    public void pingProductService() {
        if (!isPingJobEnabled) {
            log.debug("Ping job is disabled.");
            return;
        }

        try {
            log.info("Pinging product service...");
            productFeignProvider.ping();
            log.info("Ping to product service successful.");
        } catch (Exception e) {
            log.error("Error occurred while pinging product service: {}", e.getMessage(), e);
        }
    }
}
