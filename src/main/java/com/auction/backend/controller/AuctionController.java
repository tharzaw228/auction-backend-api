package com.auction.backend.controller;

import com.auction.backend.dto.AuctionRequestDto;
import com.auction.backend.dto.AuctionResponseDto;
import com.auction.backend.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @PostMapping
    public AuctionResponseDto createAuction(@RequestBody AuctionRequestDto dto, Authentication authentication) {
        String username = authentication.getName();
        return auctionService.createAuction(dto, username);
    }

    @GetMapping
    public List<AuctionResponseDto> getAllOpenAuctions() {
        return auctionService.getAllOpenAuctions();
    }

    @GetMapping("/{id}")
    public AuctionResponseDto getAuctionById(@PathVariable Long id) {
        return auctionService.getAuctionById(id);
    }

    @PutMapping("/{id}")
    public AuctionResponseDto updateAuction(@PathVariable Long id,
                                            @RequestBody AuctionRequestDto dto,
                                            Authentication authentication) {
        String email = authentication.getName();
        return auctionService.updateAuction(id, dto, email);
    }

}
