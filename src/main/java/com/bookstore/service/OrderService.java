package com.bookstore.service;

import com.bookstore.dto.OrderDtos;
import com.bookstore.entity.*;
import com.bookstore.exception.BadRequestException;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Page<OrderDtos.OrderResponse> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size)).map(this::toResponse);
    }

    public OrderDtos.OrderResponse getOrder(Long id, Authentication authentication) {
        BookOrder order = findOrder(id);
        boolean admin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && !order.getUser().getEmail().equals(authentication.getName())) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        return toResponse(order);
    }

    @Transactional
    public OrderDtos.OrderResponse placeOrder(OrderDtos.CreateOrderRequest request, Authentication authentication) {
        if (request.items().isEmpty()) {
            throw new BadRequestException("Order items cannot be empty");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BookOrder order = new BookOrder();
        order.setUser(user);
        if (request.paymentStatus() != null) {
            order.setPaymentStatus(request.paymentStatus());
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = request.items().stream().map(itemRequest -> {
            Book book = bookRepository.findById(itemRequest.bookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + itemRequest.bookId()));

            if (book.getStockQuantity() < itemRequest.quantity()) {
                throw new BadRequestException("Not enough stock for book " + book.getTitle());
            }

            book.setStockQuantity(book.getStockQuantity() - itemRequest.quantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(book.getPrice());
            item.setLineTotal(book.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
            return item;
        }).toList();

        for (OrderItem item : items) {
            total = total.add(item.getLineTotal());
        }

        order.setItems(items);
        order.setTotalPrice(total);

        return toResponse(orderRepository.save(order));
    }

    public OrderDtos.OrderResponse updateOrderStatus(Long id, OrderDtos.UpdateOrderStatusRequest request) {
        BookOrder order = findOrder(id);
        order.setOrderStatus(request.status());
        return toResponse(orderRepository.save(order));
    }

    private BookOrder findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    private OrderDtos.OrderResponse toResponse(BookOrder order) {
        return new OrderDtos.OrderResponse(
                order.getId(),
                order.getUser().getName(),
                order.getUser().getEmail(),
                order.getItems().stream().map(item -> new OrderDtos.OrderItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                )).toList(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
