package com.auction.exception;

public class AuctionProductNotFoundException extends RuntimeException {
    public AuctionProductNotFoundException(int auctionId) {
        super("Product not found for Auction with ID: " + auctionId);
    }
}
