package com.assessment.vending.controller;

import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.dto.request.ProductRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.assessment.vending.util.AppConstants.ONE_PRODUCT_URL;
import static com.assessment.vending.util.AppConstants.PRODUCTS;
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
    public BaseResponse readProduct(@PathVariable(required = false) String productName){
        return hasText(productName) ? productService.getProduct(productName) : productService.getProducts();
    }

    @PutMapping
    public BaseStandardResponse<ProductEntity> updateProduct(@Valid @RequestBody ProductRequest request){
        return productService.updateProduct(request);
    }

    @DeleteMapping("/{productName}")
    public BaseStandardResponse<ProductEntity> deleteProduct(@PathVariable String productName){
        return productService.deleteProduct(productName);
    }
}
