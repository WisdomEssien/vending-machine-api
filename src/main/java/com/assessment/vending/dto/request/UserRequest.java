package com.assessment.vending.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "firstname is required")
    @Size(max = 50, message = "firstname should be 50 characters or less")
    private String firstname;

    @NotBlank(message = "lastname is required")
    @Size(max = 50, message = "lastname should be 50 characters or less")
    private String lastname;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "email is required")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "invalid email provided")
    private String email;

    private int balance;

    @Pattern(regexp = "(?i)^(buyer|seller)$", message = "Possible values for roles: buyer, seller")
    private String role;
}
