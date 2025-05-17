package com.codewithmosh.store.Dtos;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String name;
    private String password;
    private String email;
}
