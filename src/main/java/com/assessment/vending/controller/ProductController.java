package com.assessment.vending.controller;

import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.dto.request.ProductRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.assessment.vending.util.AppConstants.ONE_PRODUCT_URL;
import static com.assessment.vending.util.AppConstants.PRODUCTS;
import static com.assessment.vending.util.ResponseCode.VALIDATION_ERROR;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RestController
@RequestMapping(PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public BaseStandardResponse<ProductEntity> createProduct(@Valid @RequestBody ProductRequest request){
        return productService.createProduct(request);
    }

    @GetMapping({"", ONE_PRODUCT_URL})
    public BaseResponse readProduct(@PathVariable(required = false) Long productID){
        return nonNull(productID) ? productService.getProduct(productID) : productService.getProducts();
    }

    @PutMapping
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody ProductRequest request){
        if(isNull(request.getProductId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseStandardResponse<>(VALIDATION_ERROR, "productId is required"));
        }
        return ResponseEntity.ok(productService.updateProduct(request));
    }

    @DeleteMapping(ONE_PRODUCT_URL)
    public BaseStandardResponse<ProductEntity> deleteProduct(@PathVariable Long productID){
        return productService.deleteProduct(productID);
    }
}
