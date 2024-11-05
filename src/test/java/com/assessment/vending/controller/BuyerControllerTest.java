package com.assessment.vending.controller;


import com.assessment.vending.database.entity.ProductEntity;
import com.assessment.vending.dto.request.BuyRequest;
import com.assessment.vending.dto.request.DepositRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.dto.response.PurchaseResponse;
import com.assessment.vending.service.BuyerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.assessment.vending.util.AppConstants.ROLE_BUYER;
import static com.assessment.vending.util.ResponseCode.INSUFFICIENT_FUNDS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BuyerController.class)
public class BuyerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BuyerService buyerService;

    private final String username = "buyerUser";


    @Test
    @WithMockUser(username = "buyerUser", roles = {ROLE_BUYER})
    void testDepositValidAmount() throws Exception {
        DepositRequest request = new DepositRequest(100, username);

        when(buyerService.deposit(any(DepositRequest.class)))
                .thenReturn(new BaseResponse("00", "Deposit successful"));

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new User(username, "password", List.of(new SimpleGrantedAuthority(ROLE_BUYER))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Deposit successful"));
    }

    @Test
    @WithMockUser(username = "buyerUser", roles = {ROLE_BUYER})
    void testDepositInvalidAmount() throws Exception {
        DepositRequest request = new DepositRequest(75, username);

        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new User(username, "password", List.of(new SimpleGrantedAuthority(ROLE_BUYER))))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").value("Invalid denomination"));
    }

    @Test
    void testDepositUnauthenticated() throws Exception {
        DepositRequest request = new DepositRequest(100, username);
        mockMvc.perform(post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "buyerUser", roles = {ROLE_BUYER})
    void testBuyValidRequest() throws Exception {
        BuyRequest request = new BuyRequest(1L, 2);
        PurchaseResponse response = PurchaseResponse.builder()
                .totalSpent(100)
                .purchasedProduct(new ProductEntity())
                .change(List.of(50))
                .build();

        when(buyerService.buy(any(BuyRequest.class), any(String.class)))
                .thenReturn(new BaseStandardResponse(response));

        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new User(username, "password", List.of(new SimpleGrantedAuthority(ROLE_BUYER))))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Successful"));
    }

    @Test
    @WithMockUser(username = "buyerUser", roles = {ROLE_BUYER})
    void testBuyInsufficientFunds() throws Exception {
        BuyRequest request = new BuyRequest(1L, 10);
        PurchaseResponse response = PurchaseResponse.builder()
                .totalSpent(100)
                .purchasedProduct(new ProductEntity())
                .change(List.of(50))
                .build();

        when(buyerService.buy(any(BuyRequest.class), any(String.class)))
                .thenReturn(new BaseStandardResponse(INSUFFICIENT_FUNDS, response));

        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(SecurityMockMvcRequestPostProcessors.user(
                                new User(username, "password", List.of(new SimpleGrantedAuthority(ROLE_BUYER))))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient funds"));
    }

    @Test
    void testBuyUnauthenticated() throws Exception {
        BuyRequest request = new BuyRequest(1L, 10);
        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }



}
