package com.bookstore.repository;

import com.bookstore.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<BookOrder, Long> {
}
