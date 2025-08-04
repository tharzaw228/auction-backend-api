package com.auction.backend.repository;

import com.auction.backend.model.Auction;
import com.auction.backend.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionOrderByBidTimeDesc(Auction auction);
}
