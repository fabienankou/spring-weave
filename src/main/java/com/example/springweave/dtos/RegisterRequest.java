package com.example.springweave.dtos;

public record RegisterRequest(
    String name,
    String email,
    String password,
    String phone,
    String country,
    String city,
    String address
) {}
