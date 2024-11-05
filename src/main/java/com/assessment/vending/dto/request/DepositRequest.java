package com.assessment.vending.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @Min(value = 50, message = "amount should not be less than 50")
    @Max(value = 1000, message = "amount should not be more than 1000")
    private int amount;

    private String username;
}
