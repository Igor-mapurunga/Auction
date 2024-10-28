package com.auction.dao;

import com.auction.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository <Product, Integer> {
}
