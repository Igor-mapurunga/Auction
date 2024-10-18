package com.auction.service;

import com.auction.entities.Auction;

import java.util.List;

public interface AuctionService {
    List<Auction> findAll();
    Auction findById(int theId);

    Auction save(Auction theUser);

    Auction deleteById(int theId);
}
