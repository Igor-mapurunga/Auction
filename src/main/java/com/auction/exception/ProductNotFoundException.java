package com.auction.exception;

public class ProductNotFoundException extends ResourceNotFoundException{
    public ProductNotFoundException(int productId) {
        super("Product not found with id - " + productId);
    }
}
