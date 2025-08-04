package com.auction.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRequestDto {
    private Long auctionId;
    private BigDecimal amount;
}
