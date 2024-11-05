package com.assessment.vending.service;

import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.dto.request.BuyRequest;
import com.assessment.vending.dto.request.DepositRequest;
import com.assessment.vending.dto.request.ProductRequest;
import com.assessment.vending.dto.request.UserRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.dto.response.PurchaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.assessment.vending.util.AppConstants.DENOMINATIONS;
import static com.assessment.vending.util.ResponseCode.SUCCESS;
import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyerService {

    private final UserService userService;
    private final ProductService productService;

    private final Map<String, Integer> history = new HashMap<>();

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
        ProductEntity productEntity = productEntityBaseStandardResponse.getData();
        BeanUtils.copyProperties(productEntity, productRequest);

        List<Integer> change = new ArrayList<>();
        if(userRequest.getBalance() >= productRequest.getPrice()) {
            if(request.getQuantity() <= productRequest.getQuantity()) {

                //Update user balance
                int newBalance = userRequest.getBalance() - productRequest.getPrice();
                userRequest.setBalance(newBalance);
                userService.updateUser(userRequest);

                //Update product quantity
                productRequest.setQuantity(productRequest.getQuantity() - request.getQuantity());
                productService.updateProduct(productRequest);

                int previousBalance = isNull(history.get(username)) ? 0 : history.get(username);
                history.put(username,  previousBalance + newBalance);

                change = splitAmount(newBalance);
            } else {
                log.info("Not enough quantity for purchase");
            }
        } else {
            log.info("Not enough balance for purchase");
        }

        log.info("Purchase finished");
        return new BaseStandardResponse<>(
                PurchaseResponse.builder()
                        .totalSpent(history.get(username))
                        .purchasedProduct(productEntity)
                        .change(change)
                        .build()
        );
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

    private List<Integer> splitAmount(int amount) {
        List<Integer> result = new ArrayList<>();

        int remainingAmount = amount;
        for (int denomination : DENOMINATIONS) {
            int count = remainingAmount / denomination;
            while (count > 0) {
                result.add(denomination);
                count--;
            }
        }
        return result;
    }

}
