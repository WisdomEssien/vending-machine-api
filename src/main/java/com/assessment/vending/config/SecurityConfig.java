package com.assessment.vending.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.assessment.vending.util.AppConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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
