package com.assessment.vending.controller;

import com.assessment.vending.database.entity.UserEntity;
import com.assessment.vending.dto.request.UserRequest;
import com.assessment.vending.dto.response.BaseResponse;
import com.assessment.vending.dto.response.BaseStandardResponse;
import com.assessment.vending.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.assessment.vending.util.AppConstants.USERS;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public BaseStandardResponse<UserEntity> createUser(@Valid @RequestBody UserRequest request){
        return userService.createUser(request);
    }

    @GetMapping({"", "/{username}"})
    public BaseResponse readUser(@PathVariable(required = false) String username){
        return hasText(username) ? userService.getUser(username) : userService.getUsers();
    }

    @PutMapping
    public BaseStandardResponse<UserEntity> updateUser(@Valid @RequestBody UserRequest request){
        return userService.updateUser(request);
    }

    @DeleteMapping("/{username}")
    public BaseStandardResponse<UserEntity> deleteUser(@PathVariable String username){
        return userService.deleteUser(username);
    }
}
