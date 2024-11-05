package com.assessment.vending.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "[product]name is required")
    @Size(max = 50, message = "[product]name should be 50 characters or less")
    private String name;

    @Min(value = 1, message = "quantity should not be less than 1")
    private int quantity;

    @Min(value = 50, message = "price should not be less than 50")
    @Max(value = 1000, message = "price should not be more than 1000")
    private int price;

    @NotNull(message = "sellerId is required")
    private Long sellerId;
}
