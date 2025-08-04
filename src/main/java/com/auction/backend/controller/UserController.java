package com.auction.backend.controller;


import com.auction.backend.dto.UserProfileResponse;
import com.auction.backend.dto.UserProfileUpdateRequest;
import com.auction.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {
        return this.userService.userProfile(authentication);
    }

    @PutMapping("/profile")
    public UserProfileResponse updateProfile(
            @RequestBody UserProfileUpdateRequest request,
            Authentication authentication) {
        return userService.updateProfile(authentication, request);
    }

}
