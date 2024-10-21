package com.auction.service;
import com.auction.entities.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(int theId);

    Product save(Product theUser,int sellerId);

    Product deleteById(int theId);

}
