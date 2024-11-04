package com.assessment.vending.database.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstname;

    @Column(length = 50, nullable = false)
    private String lastname;

    @Column(unique = true, length = 100, nullable = false)
    private String username;

    @ToString.Exclude
    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 200, nullable = false)
    private int balance;

    @Column(columnDefinition = "VARCHAR(10) CHECK (role IN ('buyer', 'seller'))")
    private String role;

}
