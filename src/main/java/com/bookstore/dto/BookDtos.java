package com.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BookDtos {

    public record BookRequest(
            @NotBlank String title,
            @NotBlank String author,
            String genre,
            @NotBlank String isbn,
            @NotNull @DecimalMin("0.0") BigDecimal price,
            String description,
            @NotNull @Min(0) Integer stockQuantity,
            String imageUrl
    ) {
    }

    public record BookResponse(
            Long id,
            String title,
            String author,
            String genre,
            String isbn,
            BigDecimal price,
            String description,
            Integer stockQuantity,
            String imageUrl
    ) {
    }
}
