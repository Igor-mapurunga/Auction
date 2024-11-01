package com.auction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BidNotFoundException extends ResourceNotFoundException{
    public BidNotFoundException(int bidId) {
        super("Did not find Bid id - " + bidId);
    }
}
