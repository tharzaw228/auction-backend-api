package com.auction.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String username;
    private String email;
    private String password;
}
