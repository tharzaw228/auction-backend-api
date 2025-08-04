package com.auction.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String sellerUsername;
    private String status;
}
