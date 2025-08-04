package com.auction.backend.service;

import com.auction.backend.dto.AuctionRequestDto;
import com.auction.backend.dto.AuctionResponseDto;
import com.auction.backend.exception.ResourceNotFoundException;
import com.auction.backend.model.*;
import com.auction.backend.repository.AuctionRepository;
import com.auction.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    public AuctionResponseDto createAuction(AuctionRequestDto dto, String email) {
        User seller = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email: " + email));

        Auction auction = Auction.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startPrice(dto.getStartPrice())
                .currentPrice(dto.getStartPrice())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(AuctionStatus.OPEN)
                .seller(seller)
                .build();

        Auction saved = auctionRepository.save(auction);
        return mapToDto(saved);
    }

    public List<AuctionResponseDto> getAllOpenAuctions() {
        List<Auction> auctions = auctionRepository.findByStatus(AuctionStatus.OPEN);
        return auctions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public AuctionResponseDto getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
        return mapToDto(auction);
    }

    private AuctionResponseDto mapToDto(Auction auction) {
        AuctionResponseDto dto = new AuctionResponseDto();
        dto.setId(auction.getId());
        dto.setTitle(auction.getTitle());
        dto.setDescription(auction.getDescription());
        dto.setCurrentPrice(auction.getCurrentPrice());
        dto.setStartTime(auction.getStartTime());
        dto.setEndTime(auction.getEndTime());
        dto.setSellerUsername(auction.getSeller().getUsername());
        dto.setStatus(auction.getStatus().name());
        return dto;
    }

    @Transactional
    public AuctionResponseDto updateAuction(Long id, AuctionRequestDto dto, String email) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        // Check if current user is the seller
        if (!auction.getSeller().getEmail().equals(email)) {
            throw new IllegalStateException("You are not authorized to update this auction.");
        }

        // Optional: Check auction status
        if (auction.getStatus() != AuctionStatus.OPEN) {
            throw new IllegalStateException("Auction cannot be updated after it is closed.");
        }

        // Optional: Prevent update if auction has already started (startTime < now)
        if (auction.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Auction already started. Update not allowed.");
        }

        // Update only non-null fields
        if (dto.getTitle() != null) {
            auction.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            auction.setDescription(dto.getDescription());
        }

        if (dto.getStartPrice() != null) {
            auction.setStartPrice(dto.getStartPrice());
            auction.setCurrentPrice(dto.getStartPrice());  // reset only if start price changes
        }

        if (dto.getStartTime() != null) {
            auction.setStartTime(dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            auction.setEndTime(dto.getEndTime());
        }

        Auction updated = auctionRepository.save(auction);
        return mapToDto(updated);
    }

}
