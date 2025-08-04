package com.auction.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BidResponseDto {
    private Long id;
    private String bidderUsername;
    private BigDecimal amount;
    private LocalDateTime bidTime;
}
