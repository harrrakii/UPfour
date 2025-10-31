package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private String bookTitle;
    private String authorName;
    private String publisherName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
