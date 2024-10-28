package com.auction.controller;

import com.auction.entities.Product;
import com.auction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts () {
        return productService.findAll();
    }
    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable int productId) {
        return productService.findById(productId);
    }
    @PostMapping("/addNewProduct/{sellerId}")
    @Transactional
    public Product createProduct(@RequestBody Product theProduct,@PathVariable int sellerId) {
        return productService.save(theProduct,sellerId);
    }
    @DeleteMapping("/removeProduct/{productId}")
    @Transactional
    public Product deleteProduct (@PathVariable int productId) {
        Product theProduct = productService.deleteById(productId);
        return theProduct;
    }
    @PutMapping("/updateProduct/{productId}/{sellerId}")
    @Transactional
    public ResponseEntity<Product> updateProduct(@PathVariable int productId, @RequestBody Product theProduct, @PathVariable int sellerId) {
        Product updatedProduct = productService.updateProduct(productId, theProduct, sellerId);
        return updatedProduct != null ? ResponseEntity.ok(updatedProduct) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
