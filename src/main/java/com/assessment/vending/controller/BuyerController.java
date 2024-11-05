package com.assessment.vending.controller;

import com.assessment.vending.dto.request.BuyRequest;
import com.assessment.vending.dto.request.DepositRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;

import static com.assessment.vending.util.AppConstants.*;
import static com.assessment.vending.util.ResponseCode.VALIDATION_ERROR;
import static java.util.Objects.isNull;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping(DEPOSIT)
    public ResponseEntity<Object> deposit(@Valid @RequestBody DepositRequest request,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (Arrays.asList(DENOMINATIONS).contains(request.getAmount())) {
            request.setUsername(isNull(request.getUsername()) ? userDetails.getUsername() : request.getUsername());
            return ResponseEntity.ok(buyerService.deposit(request));
        }
        return ResponseEntity.badRequest()
                .body(new BaseStandardResponse<>(VALIDATION_ERROR, "Invalid denomination"));
    }

    @PostMapping(BUY)
    public ResponseEntity<BaseStandardResponse> buy(@Valid @RequestBody BuyRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(buyerService.buy(request, userDetails.getUsername()));
    }

    @PostMapping(RESET)
    public ResponseEntity<BaseResponse> resetBalance(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(buyerService.resetBalance(userDetails.getUsername()));
    }
}
