package com.auction.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionRequestDto {
    private String title;
    private String description;
    private BigDecimal startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
