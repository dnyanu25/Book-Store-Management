package com.bookstore.controller;

import com.bookstore.dto.BookDtos;
import com.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookDtos.BookResponse>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(bookService.getBooks(page, size, q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDtos.BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PostMapping
    public ResponseEntity<BookDtos.BookResponse> createBook(@Valid @RequestBody BookDtos.BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDtos.BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookDtos.BookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
