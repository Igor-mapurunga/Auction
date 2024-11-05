package com.auction.exception;

public class AuctionStartDatePassedException extends RuntimeException{
    public AuctionStartDatePassedException(String message) {
        super(message);
    }
}
