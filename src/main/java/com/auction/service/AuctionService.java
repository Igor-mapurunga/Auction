package com.auction.service;

import com.auction.dto.AuctionDTO;
import com.auction.entities.Auction;
import java.util.List;

public interface AuctionService {
    List<Auction> findAll();
    Auction findById(int auctionId);

    Auction createAuction(int productId, AuctionDTO auctionDTO);

    void deleteById(int auctionId);
}
