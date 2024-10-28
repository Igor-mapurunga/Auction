package com.auction.service;

import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Product;
import com.auction.entities.User;
import com.auction.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    @Override
    public Product findById(int theId) {
        Optional<Product> result = productRepository.findById(theId);
        Product theProduct = null;
        if (result.isPresent()) {
            theProduct = result.get();
        }else {
            throw new RuntimeException("Did not find product id - " + theId);
        }
        return theProduct;
    }

    @Override
    public Product save(Product theProduct, int sellerId) {
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));
        theProduct.setSeller(seller);
        return productRepository.save(theProduct);
    }

    @Override
    public Product updateProduct(int productId, Product theProduct, int sellerId) {
        Product productToUpdate = findById(productId);
        User seller = userRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found with id - " + sellerId));
        theProduct.setSeller(seller);
        theProduct.setId(productId);
        return productRepository.save(theProduct);
    }
    @Override
    public Product deleteById(int theId) {
        productRepository.deleteById(theId);
        return null;
    }
}

