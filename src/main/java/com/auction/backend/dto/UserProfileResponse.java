package com.auction.backend.dto;

import com.auction.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
}
