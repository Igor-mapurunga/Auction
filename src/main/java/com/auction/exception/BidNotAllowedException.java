package com.auction.exception;

public class BidNotAllowedException extends RuntimeException {

    public BidNotAllowedException(String message) {
        super(message);
    }
}