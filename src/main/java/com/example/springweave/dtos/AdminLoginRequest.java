package com.example.springweave.dtos;

public record AdminLoginRequest(String email, String password, String totpCode) {}
