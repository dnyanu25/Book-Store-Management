package com.bookstore.service;

import com.bookstore.dto.BookDtos;
import com.bookstore.entity.Book;
import com.bookstore.exception.ResourceNotFoundException;
import com.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookDtos.BookResponse> getBooks(int page, int size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = (query == null || query.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query, pageable);

        return books.map(this::toResponse);
    }

    public BookDtos.BookResponse getBook(Long id) {
        return toResponse(findById(id));
    }

    public BookDtos.BookResponse createBook(BookDtos.BookRequest request) {
        Book book = new Book();
        updateEntity(book, request);
        return toResponse(bookRepository.save(book));
    }

    public BookDtos.BookResponse updateBook(Long id, BookDtos.BookRequest request) {
        Book book = findById(id);
        updateEntity(book, request);
        return toResponse(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    private Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    private void updateEntity(Book book, BookDtos.BookRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setGenre(request.genre());
        book.setIsbn(request.isbn());
        book.setPrice(request.price());
        book.setDescription(request.description());
        book.setStockQuantity(request.stockQuantity());
        book.setImageUrl(request.imageUrl());
    }

    private BookDtos.BookResponse toResponse(Book book) {
        return new BookDtos.BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getStockQuantity(),
                book.getImageUrl()
        );
    }
}
