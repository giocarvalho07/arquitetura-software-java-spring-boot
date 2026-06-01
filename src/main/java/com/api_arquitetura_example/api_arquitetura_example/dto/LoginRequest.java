package com.api_arquitetura_example.api_arquitetura_example.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}