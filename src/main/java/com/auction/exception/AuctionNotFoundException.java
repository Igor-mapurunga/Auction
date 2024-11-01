package com.auction.exception;

public class AuctionNotFoundException extends ResourceNotFoundException{
    public AuctionNotFoundException(int auctionId) {
        super("Auction not found with id - " + auctionId);
    }
}
