package com.auction.backend.controller;

import com.auction.backend.dto.BidRequestDto;
import com.auction.backend.dto.BidResponseDto;
import com.auction.backend.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @PostMapping
    public BidResponseDto placeBid(@RequestBody BidRequestDto dto,
                                   Authentication authentication) {
        String email = authentication.getName();
        return bidService.placeBid(dto, email);
    }
}
