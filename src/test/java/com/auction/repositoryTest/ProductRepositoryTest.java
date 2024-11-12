package com.auction.repositoryTest;

import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Product;
import com.auction.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @Rollback(false)
    public void testSaveProduct() {
        User seller = new User();
        seller.setName("Seller John");
        seller.setPassword("sellerpassword");
        seller.setPhoneNumber("123456789");
        seller.setAddress("123 Main St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Laptop");
        product.setStartingPrice(500.0f);
        product.setSellPrice(600.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getId() > 0);
        assertEquals("Laptop", savedProduct.getName());
        assertEquals(seller.getId(), savedProduct.getSeller().getId());
    }

    @Test
    public void testFindProductById() {
        User seller = new User();
        seller.setName("Seller Jane");
        seller.setPassword("password");
        seller.setPhoneNumber("987654321");
        seller.setAddress("456 Another St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Smartphone");
        product.setStartingPrice(300.0f);
        product.setSellPrice(350.0f);
        product.setStatus("SOLD");
        product.setCategory("Mobile");
        product.setSeller(seller);
        product = productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertTrue(foundProduct.isPresent());
        assertEquals("Smartphone", foundProduct.get().getName());
    }
    @Test
    public void testFindAllProducts() {
        User seller = new User();
        seller.setName("Seller Bob");
        seller.setPassword("password123");
        seller.setPhoneNumber("123123123");
        seller.setAddress("789 Street One");
        seller = userRepository.save(seller);
        Product product1 = new Product();
        product1.setName("Table");
        product1.setStartingPrice(100.0f);
        product1.setSellPrice(120.0f);
        product1.setStatus("AVAILABLE");
        product1.setCategory("Furniture");
        product1.setSeller(seller);
        productRepository.save(product1);
        Product product2 = new Product();
        product2.setName("Chair");
        product2.setStartingPrice(50.0f);
        product2.setSellPrice(60.0f);
        product2.setStatus("AVAILABLE");
        product2.setCategory("Furniture");
        product2.setSeller(seller);
        productRepository.save(product2);
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty());
        assertTrue(products.size() >= 2);
    }

    @Test
    @Rollback(false)
    public void testUpdateProduct() {
        User seller = new User();
        seller.setName("Seller Alice");
        seller.setPassword("alicepassword");
        seller.setPhoneNumber("456456456");
        seller.setAddress("456 Update St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("TV");
        product.setStartingPrice(400.0f);
        product.setSellPrice(450.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(seller);
        product = productRepository.save(product);
        Product productToUpdate = productRepository.findById(product.getId()).get();
        productToUpdate.setName("Updated TV");
        productToUpdate.setSellPrice(500.0f);
        Product updatedProduct = productRepository.save(productToUpdate);
        assertEquals("Updated TV", updatedProduct.getName());
        assertEquals(500.0f, updatedProduct.getSellPrice());
    }

    @Test
    @Rollback(false)
    public void testDeleteProduct() {
        User seller = new User();
        seller.setName("Seller Tom");
        seller.setPassword("tomseller");
        seller.setPhoneNumber("789789789");
        seller.setAddress("789 Delete St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Camera");
        product.setStartingPrice(150.0f);
        product.setSellPrice(180.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Photography");
        product.setSeller(seller);
        product = productRepository.save(product);
        int productId = product.getId();
        productRepository.deleteById(productId);
        Optional<Product> deletedProduct = productRepository.findById(productId);
        assertFalse(deletedProduct.isPresent());
    }
}