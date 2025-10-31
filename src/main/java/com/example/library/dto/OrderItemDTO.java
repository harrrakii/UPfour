package com.example.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    
    @NotNull
    private Long productId;
    
    @NotNull
    @Min(value = 1)
    private Integer quantity;
    
    @NotNull
    private BigDecimal price;
    
    private String productName;
    private BigDecimal subtotal;
}

