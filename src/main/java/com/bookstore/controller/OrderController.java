package com.bookstore.controller;

import com.bookstore.dto.OrderDtos;
import com.bookstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderDtos.OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDtos.OrderResponse> getOrder(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrder(id, authentication));
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderDtos.OrderResponse> placeOrder(@Valid @RequestBody OrderDtos.CreateOrderRequest request,
                                                              Authentication authentication) {
        return ResponseEntity.ok(orderService.placeOrder(request, authentication));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDtos.OrderResponse> updateStatus(@PathVariable Long id,
                                                                @Valid @RequestBody OrderDtos.UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }
}
