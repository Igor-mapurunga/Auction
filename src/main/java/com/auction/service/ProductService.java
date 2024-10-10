package com.auction.service;

import com.auction.entities.Product;
import com.auction.entities.User;

import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(int theId);

    Product save(Product theUser,int sellerId);

    Product deleteById(int theId);

}
