package com.assessment.vending.service;

import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.database.repository.ProductRepository;
import com.assessment.vending.dto.request.ProductRequest;
import com.assessment.vending.dto.request.UserRequest;
import com.assessment.vending.dto.response.BaseCollectionResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.assessment.vending.util.AppConstants.EM_SAVING_TO_DATABASE;
import static com.assessment.vending.util.ResponseCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    protected final ProductRepository productRepository;

    public BaseStandardResponse<ProductEntity> createProduct(ProductRequest request) {
        log.info("Creating new productEntity");
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(request, productEntity);

        BaseStandardResponse<ProductEntity> reponse;
        try {
            reponse = new BaseStandardResponse<>(productRepository.save(productEntity));
            log.info("Saved new productEntity to the database");
        } catch (Exception ex) {
            log.error(EM_SAVING_TO_DATABASE, ex);
            reponse = new BaseStandardResponse<>(SAVE_TO_DATABASE_ERROR);
        }
        log.info("Final response :: {}", reponse);
        return reponse;
    }

    public BaseStandardResponse<ProductEntity> updateProduct(ProductRequest request) {
        log.info("Updating existing product with name {}", request.getName());
        Optional<ProductEntity> optionalProductEntity = productRepository.findByName(request.getName());
        if (optionalProductEntity.isEmpty()) {
            log.info("Product name, {}, is not found", request.getName());
            return new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD);
        }

        ProductEntity productEntity = optionalProductEntity.get();
        BeanUtils.copyProperties(request, productEntity);

        BaseStandardResponse<ProductEntity> reponse;
        try {
            reponse = new BaseStandardResponse<>(productRepository.save(productEntity));
            log.info("Updated product in the database");
        } catch (Exception ex) {
            log.error(EM_SAVING_TO_DATABASE, ex);
            reponse = new BaseStandardResponse<>(SAVE_TO_DATABASE_ERROR);
        }
        log.info("Final response :: {}", reponse);
        return reponse;
    }

    public BaseCollectionResponse<ProductEntity> getProducts() {
        log.info("Get all products");
        return new BaseCollectionResponse<>(productRepository.findAll());
    }

    public BaseStandardResponse<ProductEntity> getProduct(String productName) {
        log.info("Get product with name {}", productName);
        Optional<ProductEntity> optionalProductEntity = productRepository.findByName(productName);
        return optionalProductEntity
                .map(BaseStandardResponse::new)
                .orElseGet(() -> new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD));
    }

    public BaseStandardResponse<ProductEntity> deleteProduct(String productName) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findByName(productName);
        if (optionalProductEntity.isEmpty()) {
            return new BaseStandardResponse<>(UNABLE_TO_LOCATE_RECORD);
        }
        productRepository.deleteById(optionalProductEntity.get().getId());
        log.info("Deleted product with name {}", productName);
        return new BaseStandardResponse<>(SUCCESS);
    }

}