package com.auction;

import com.auction.controller.ProductController;
import com.auction.entities.Product;
import com.auction.exception.GlobalExceptionHandler;
import com.auction.exception.ProductNotFoundException;
import com.auction.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(productService, times(1)).findAll();
    }

    @Test
    public void testGetProductById() throws Exception {
        Product product = new Product();
        product.setId(1);
        when(productService.findById(1)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(productService, times(1)).findById(1);
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productService.findById(1)).thenThrow(new ProductNotFoundException(1));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).findById(1);
    }

    @Test
    public void testCreateProduct() throws Exception {
        int sellerId = 1;
        Product product = new Product();
        product.setName("Test Product");

        when(productService.save(any(Product.class), eq(sellerId))).thenReturn(product);

        mockMvc.perform(post("/products/addNewProduct/{sellerId}", sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService, times(1)).save(any(Product.class), eq(sellerId));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);

        when(productService.deleteById(productId)).thenReturn(product);

        mockMvc.perform(delete("/products/removeProduct/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));

        verify(productService, times(1)).deleteById(productId);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        int productId = 1;
        int sellerId = 1;
        Product product = new Product();
        product.setName("Updated Product");

        when(productService.updateProduct(eq(productId), any(Product.class), eq(sellerId))).thenReturn(product);

        mockMvc.perform(put("/products/updateProduct/{productId}/{sellerId}", productId, sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));

        verify(productService, times(1)).updateProduct(eq(productId), any(Product.class), eq(sellerId));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        int productId = 1;
        int sellerId = 1;
        Product product = new Product();
        product.setName("Updated Product");

        when(productService.updateProduct(eq(productId), any(Product.class), eq(sellerId))).thenReturn(null);

        mockMvc.perform(put("/products/updateProduct/{productId}/{sellerId}", productId, sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(productId), any(Product.class), eq(sellerId));
    }
}

