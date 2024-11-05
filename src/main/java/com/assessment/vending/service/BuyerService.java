package com.assessment.vending.service;

import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.dto.request.BuyRequest;
import com.assessment.vending.dto.request.DepositRequest;
import com.assessment.vending.dto.request.ProductRequest;
import com.assessment.vending.dto.request.UserRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.assessment.vending.util.ResponseCode.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerService {

    private final UserService userService;
    private final ProductService productService;

    public BaseResponse deposit(DepositRequest request) {
        log.info("Deposit request :: {}", request);
        BaseStandardResponse<UserEntity> userEntityBaseStandardResponse = userService.getUser(request.getUsername());
        if (SUCCESS.getCode().equals(userEntityBaseStandardResponse.getResponseCode())) {
            UserRequest userRequest = new UserRequest();
            BeanUtils.copyProperties(userEntityBaseStandardResponse.getData(), userRequest);
            userRequest.setBalance(userRequest.getBalance() + request.getAmount());
            log.info("New balance = {}", userRequest.getBalance());
            return userService.updateUser(userRequest);
        }
        log.info("Deposit request :: {}", request);
        return userEntityBaseStandardResponse;
    }

    @Transactional
    public BaseResponse buy(BuyRequest request, String username) {
        log.info("Buy request = {} :: username = {}", request, username);
        BaseStandardResponse<UserEntity> userEntityBaseStandardResponse = userService.getUser(username);
        if (!SUCCESS.getCode().equals(userEntityBaseStandardResponse.getResponseCode())) {
            return userEntityBaseStandardResponse;
        }

        BaseStandardResponse<ProductEntity> productEntityBaseStandardResponse = productService.getProduct(request.getProductId());
        if (!SUCCESS.getCode().equals(productEntityBaseStandardResponse.getResponseCode())) {
            return productEntityBaseStandardResponse;
        }

        UserRequest userRequest = new UserRequest();
        BeanUtils.copyProperties(userEntityBaseStandardResponse.getData(), userRequest);

        ProductRequest productRequest = new ProductRequest();
        BeanUtils.copyProperties(productEntityBaseStandardResponse.getData(), productRequest);

        if(userRequest.getBalance() >= productRequest.getPrice()) {
            log.info("Enough balance for purchase");
            if(request.getQuantity() <= productRequest.getQuantity()) {
                log.info("Enough quantity for purchase");

            }

        }
        userRequest.setBalance(userRequest.getBalance() - productRequest.getPrice());
        log.info("New balance = {}", userRequest.getBalance());
        return userService.updateUser(userRequest);

        log.info("Deposit request :: {}", request);
        return userEntityBaseStandardResponse;
    }

    public BaseResponse resetBalance(String username) {
        BaseStandardResponse<UserEntity> userEntityBaseStandardResponse = userService.getUser(username);
        if (SUCCESS.getCode().equals(userEntityBaseStandardResponse.getResponseCode())) {
            UserRequest userRequest = new UserRequest();
            BeanUtils.copyProperties(userEntityBaseStandardResponse.getData(), userRequest);
            userRequest.setBalance(0);
            log.info("New balance = {}", userRequest.getBalance());
            return userService.updateUser(userRequest);
        }
        return userEntityBaseStandardResponse;
    }

}
