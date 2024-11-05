package com.assessment.vending.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyRequest {

    @Min(value = 1, message = "productId should not be less than 1")
    @Positive(message = "productId should be a positive number")
    private Long productId;

    @Min(value = 1, message = "quantity should not be less than 1")
    @Positive(message = "quantity should be a positive number")
    private int quantity;
}
