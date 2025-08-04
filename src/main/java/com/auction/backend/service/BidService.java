package com.auction.backend.service;

import com.auction.backend.dto.BidRequestDto;
import com.auction.backend.dto.BidResponseDto;
import com.auction.backend.exception.ResourceNotFoundException;
import com.auction.backend.model.Auction;
import com.auction.backend.model.Bid;
import com.auction.backend.model.User;
import com.auction.backend.repository.AuctionRepository;
import com.auction.backend.repository.BidRepository;
import com.auction.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public BidResponseDto placeBid(BidRequestDto dto, String bidderEmail) {
        Auction auction = auctionRepository.findById(dto.getAuctionId())
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (auction.getStatus() != com.auction.backend.model.AuctionStatus.OPEN) {
            throw new IllegalStateException("Auction is not open for bidding.");
        }

        if (auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Auction has already ended.");
        }

        User bidder = userRepository.findByEmail(bidderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (auction.getSeller().getId().equals(bidder.getId())) {
            throw new IllegalStateException("Sellers cannot bid on their own auctions.");
        }

        BigDecimal currentPrice = auction.getCurrentPrice();
        if (dto.getAmount().compareTo(currentPrice.add(BigDecimal.ONE)) < 0) {
            throw new IllegalStateException("Bid must be at least $1 higher than current price.");
        }

        Bid bid = Bid.builder()
                .auction(auction)
                .bidder(bidder)
                .amount(dto.getAmount())
                .bidTime(LocalDateTime.now())
                .build();

        bidRepository.save(bid);

        // Update auction price
        auction.setCurrentPrice(dto.getAmount());
        auctionRepository.save(auction);

        BidResponseDto response = new BidResponseDto();
        response.setId(bid.getId());
        response.setBidderUsername(bidder.getUsername());
        response.setAmount(bid.getAmount());
        response.setBidTime(bid.getBidTime());

        return response;
    }
}
