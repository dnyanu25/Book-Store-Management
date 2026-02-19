package com.bookstore.dto;

import com.bookstore.entity.OrderStatus;
import com.bookstore.entity.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDtos {

    public record OrderItemRequest(
            @NotNull Long bookId,
            @NotNull @Min(1) Integer quantity
    ) {
    }

    public record CreateOrderRequest(
            @NotNull List<OrderItemRequest> items,
            PaymentStatus paymentStatus
    ) {
    }

    public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {
    }

    public record OrderItemResponse(
            Long bookId,
            String title,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {
    }

    public record OrderResponse(
            Long id,
            String customerName,
            String customerEmail,
            List<OrderItemResponse> items,
            OrderStatus orderStatus,
            PaymentStatus paymentStatus,
            BigDecimal totalPrice,
            LocalDateTime createdAt
    ) {
    }
}
