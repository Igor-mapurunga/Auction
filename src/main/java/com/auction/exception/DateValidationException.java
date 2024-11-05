package com.auction.exception;

public class DateValidationException extends IllegalArgumentException{
    public DateValidationException(String message) {
        super(message);
    }
}
