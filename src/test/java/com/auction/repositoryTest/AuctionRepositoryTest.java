package com.auction.repositoryTest;
import com.auction.dao.AuctionRepository;
import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Auction;
import com.auction.entities.Product;
import com.auction.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuctionRepositoryTest {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @Rollback(false)
    public void testSaveAuction() {
        User seller = new User();
        seller.setName("John Doe");
        seller.setPassword("password123");
        seller.setPhoneNumber("123456789");
        seller.setAddress("123 Main St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Bicycle");
        product.setStartingPrice(200.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Sports");
        product.setSeller(seller);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        Auction savedAuction = auctionRepository.save(auction);
        assertNotNull(savedAuction);
        assertTrue(savedAuction.getId() > 0);
        assertEquals("ONGOING", savedAuction.getStatus());
        assertEquals(product.getId(), savedAuction.getProduct().getId());
    }
    @Test
    public void testFindAuctionById() {
        User seller = new User();
        seller.setName("Jane Doe");
        seller.setPassword("123456");
        seller.setPhoneNumber("987654321");
        seller.setAddress("456 Another St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Laptop");
        product.setStartingPrice(500.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(seller);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("PENDING");
        auction = auctionRepository.save(auction);
        Optional<Auction> foundAuction = auctionRepository.findById(auction.getId());
        assertTrue(foundAuction.isPresent());
        assertEquals("PENDING", foundAuction.get().getStatus());
    }
    @Test
    public void testFindAllAuctions() {
        User seller = new User();
        seller.setName("Mark Doe");
        seller.setPassword("123456");
        seller.setPhoneNumber("123123123");
        seller.setAddress("789 Some St");
        seller = userRepository.save(seller);
        Product product1 = new Product();
        product1.setName("Table");
        product1.setStartingPrice(100.0f);
        product1.setSellPrice(0.0f);
        product1.setStatus("AVAILABLE");
        product1.setCategory("Furniture");
        product1.setSeller(seller);
        productRepository.save(product1);
        Product product2 = new Product();
        product2.setName("Chair");
        product2.setStartingPrice(50.0f);
        product2.setSellPrice(0.0f);
        product2.setStatus("AVAILABLE");
        product2.setCategory("Furniture");
        product2.setSeller(seller);
        productRepository.save(product2);
        Auction auction1 = new Auction();
        auction1.setStartDate(new Date());
        auction1.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction1.setProduct(product1);
        auction1.setStatus("COMPLETED");
        auctionRepository.save(auction1);
        Auction auction2 = new Auction();
        auction2.setStartDate(new Date());
        auction2.setEndDate(new Date(System.currentTimeMillis() + 172800000));
        auction2.setProduct(product2);
        auction2.setStatus("ONGOING");
        auctionRepository.save(auction2);
        List<Auction> auctions = auctionRepository.findAll();
        assertFalse(auctions.isEmpty());
        assertTrue(auctions.size() >= 2);
    }

    @Test
    @Rollback(false)
    public void testUpdateAuction() {
        User seller = new User();
        seller.setName("Jaimie Doe");
        seller.setPassword("123456");
        seller.setPhoneNumber("456456456");
        seller.setAddress("123 First St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Television");
        product.setStartingPrice(400);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(seller);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Auction auctionToUpdate = auctionRepository.findById(auction.getId()).get();
        auctionToUpdate.setStatus("COMPLETED");
        Auction updatedAuction = auctionRepository.save(auctionToUpdate);
        assertEquals("COMPLETED", updatedAuction.getStatus());
    }
    @Test
    @Rollback(false)
    public void testDeleteAuction() {
        User seller = new User();
        seller.setName("Jonathan Doe");
        seller.setPassword("123456");
        seller.setPhoneNumber("85998372055");
        seller.setAddress("789 Avenue St");
        seller = userRepository.save(seller);
        Product product = new Product();
        product.setName("Camera");
        product.setStartingPrice(150);
        product.setSellPrice(0);
        product.setStatus("AVAILABLE");
        product.setCategory("Photography");
        product.setSeller(seller);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        int auctionId = auction.getId();
        auctionRepository.deleteById(auctionId);
        Optional<Auction> deletedAuction = auctionRepository.findById(auctionId);
        assertFalse(deletedAuction.isPresent());
    }
}