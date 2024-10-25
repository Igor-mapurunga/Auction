package com.auction.service;

import com.auction.entities.Auction;
import com.auction.entities.Bid;

import java.util.List;

public interface BidService {
    List<Bid> findAll();
    Bid findById(int theId);
    Bid save(Bid theBid);
    Bid deleteById(int theId);
    String createBid(int userId, int auctionId, Bid bidRequest);
}
