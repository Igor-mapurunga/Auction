package com.auction.serviceTest;

import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Product;
import com.auction.entities.User;
import com.auction.exception.ResourceNotFoundException;
import com.auction.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllProducts() {
        productService.findAll();
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testFindProductById_Success() {
        Product product = new Product();
        product.setId(1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(1);
        assertNotNull(foundProduct);
        assertEquals(1, foundProduct.getId());

        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void testFindProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.findById(1));
        assertEquals("Did not find product id - 1", thrown.getMessage());

        verify(productRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveProduct_Success() {
        int sellerId = 1;
        Product product = new Product();
        User seller = new User();
        seller.setId(sellerId);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product, sellerId);

        assertNotNull(savedProduct);
        verify(productRepository, times(1)).save(any(Product.class));
        verify(userRepository, times(1)).findById(sellerId);
    }

    @Test
    public void testSaveProduct_SellerNotFound() {
        int sellerId = 1;
        Product product = new Product();

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.save(product, sellerId));
        assertEquals("Seller not found", thrown.getMessage());

        verify(userRepository, times(1)).findById(sellerId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_Success() {
        int productId = 1;
        int sellerId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        User seller = new User();
        seller.setId(sellerId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId, updatedProduct, sellerId);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productRepository, times(1)).findById(productId);
        verify(userRepository, times(1)).findById(sellerId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_ProductNotFound() {
        int productId = 1;
        int sellerId = 1;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.updateProduct(productId, product, sellerId));
        assertEquals("Did not find product id - " + productId, thrown.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(userRepository, never()).findById(sellerId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_SellerNotFound() {
        int productId = 1;
        int sellerId = 1;
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, product, sellerId));
        assertEquals("Seller not found with id - " + sellerId, thrown.getMessage());

        verify(productRepository, times(1)).findById(productId);
        verify(userRepository, times(1)).findById(sellerId);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProductById_Success() {
        Product product = new Product();
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product deletedProduct = productService.deleteById(1);

        assertEquals(product, deletedProduct);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteProductById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.deleteById(1));
        assertEquals("Product not found with id - 1", thrown.getMessage());

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, never()).deleteById(anyInt());
    }
}