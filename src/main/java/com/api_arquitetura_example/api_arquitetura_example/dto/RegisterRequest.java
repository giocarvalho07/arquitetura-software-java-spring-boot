package com.api_arquitetura_example.api_arquitetura_example.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
}

