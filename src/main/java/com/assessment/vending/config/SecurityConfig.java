package com.assessment.vending.config;


import com.assessment.vending.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.List;

import static com.assessment.vending.util.AppConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(ROLE_BUYER, ROLE_SELLER)
                .build();

        List<UserDetails> userDetailsList = new java.util.ArrayList<>(userService.getUsers().getData()
                .stream().map(userEntity -> User.withUsername(userEntity.getUsername())
                        .password(userEntity.getPassword())
                        .roles(userEntity.getRole())
                        .build())
                .toList());
        userDetailsList.add(admin);

        UserDetails[] userDetails = userDetailsList.toArray(UserDetails[]::new);

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, USERS).permitAll()
                        .requestMatchers(HttpMethod.POST, PRODUCTS).hasAuthority(ROLE_SELLER)
                        .requestMatchers(HttpMethod.PUT, ONE_PRODUCT_URL).hasAuthority(ROLE_SELLER)
                        .requestMatchers(HttpMethod.DELETE, ONE_PRODUCT_URL).hasAuthority(ROLE_SELLER)
                        .requestMatchers(DEPOSIT, BUY, RESET).hasAuthority(ROLE_BUYER)
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
