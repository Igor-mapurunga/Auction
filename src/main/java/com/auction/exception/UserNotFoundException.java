package com.auction.exception;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException(int userId) {
        super("Did not find user id - " + userId);
    }
}
