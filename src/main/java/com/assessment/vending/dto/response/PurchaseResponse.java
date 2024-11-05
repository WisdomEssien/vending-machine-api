package com.assessment.vending.dto.response;

import com.assessment.vending.database.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {
    private int totalSpent;
    private ProductEntity purchasedProduct;
    private List<Integer> change;
}
