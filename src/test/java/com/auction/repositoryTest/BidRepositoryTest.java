package com.auction.repositoryTest;

import com.auction.dao.AuctionRepository;
import com.auction.dao.BidRepository;
import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Auction;
import com.auction.entities.Bid;
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
public class BidRepositoryTest {
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Test
    @Rollback(false)
    public void testSaveBid() {
        User user = new User();
        user.setName("Bidder One");
        user.setPassword("password123");
        user.setPhoneNumber("123456789");
        user.setAddress("123 Main St");
        user = userRepository.save(user);
        Product product = new Product();
        product.setName("Bicycle");
        product.setStartingPrice(200.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Sports");
        product.setSeller(user);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Bid bid = new Bid();
        bid.setValue(250.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(user);
        bid.setProduct(product);
        bid.setAuction(auction);
        Bid savedBid = bidRepository.save(bid);
        assertNotNull(savedBid);
        assertTrue(savedBid.getId() > 0);
        assertEquals(250.0f, savedBid.getValue());
        assertEquals(user.getId(), savedBid.getUser().getId());
        assertEquals(product.getId(), savedBid.getProduct().getId());
        assertEquals(auction.getId(), savedBid.getAuction().getId());
    }
    @Test
    public void testFindBidById() {
        User user = new User();
        user.setName("Bidder Two");
        user.setPassword("password123");
        user.setPhoneNumber("987654321");
        user.setAddress("456 Another St");
        user = userRepository.save(user);
        Product product = new Product();
        product.setName("Laptop");
        product.setStartingPrice(500.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(user);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Bid bid = new Bid();
        bid.setValue(600.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(user);
        bid.setProduct(product);
        bid.setAuction(auction);
        bid = bidRepository.save(bid);
        Optional<Bid> foundBid = bidRepository.findById(bid.getId());
        assertTrue(foundBid.isPresent());
        assertEquals(600.0f, foundBid.get().getValue());
    }
    @Test
    public void testFindAllBids() {
        User user = new User();
        user.setName("Bidder Three");
        user.setPassword("password123");
        user.setPhoneNumber("1122334455");
        user.setAddress("789 Some St");
        user = userRepository.save(user);
        Product product = new Product();
        product.setName("Smartphone");
        product.setStartingPrice(300.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(user);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Bid bid1 = new Bid();
        bid1.setValue(350.0f);
        bid1.setTimeStamp(new Date());
        bid1.setUser(user);
        bid1.setProduct(product);
        bid1.setAuction(auction);
        bidRepository.save(bid1);
        Bid bid2 = new Bid();
        bid2.setValue(400.0f);
        bid2.setTimeStamp(new Date());
        bid2.setUser(user);
        bid2.setProduct(product);
        bid2.setAuction(auction);
        bidRepository.save(bid2);
        List<Bid> bids = bidRepository.findAll();
        assertFalse(bids.isEmpty());
        assertTrue(bids.size() >= 2);
    }
    @Test
    @Rollback(false)
    public void testUpdateBid() {
        User user = new User();
        user.setName("Bidder Four");
        user.setPassword("password123");
        user.setPhoneNumber("9988776655");
        user.setAddress("123 Update St");
        user = userRepository.save(user);
        Product product = new Product();
        product.setName("Tablet");
        product.setStartingPrice(100.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Electronics");
        product.setSeller(user);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Bid bid = new Bid();
        bid.setValue(120.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(user);
        bid.setProduct(product);
        bid.setAuction(auction);
        bid = bidRepository.save(bid);
        Bid bidToUpdate = bidRepository.findById(bid.getId()).get();
        bidToUpdate.setValue(150.0f);
        Bid updatedBid = bidRepository.save(bidToUpdate);
        assertEquals(150.0f, updatedBid.getValue());
    }
    @Test
    @Rollback(false)
    public void testDeleteBid() {
        User user = new User();
        user.setName("Bidder Five");
        user.setPassword("password123");
        user.setPhoneNumber("5566778899");
        user.setAddress("456 Delete St");
        user = userRepository.save(user);
        Product product = new Product();
        product.setName("Headphones");
        product.setStartingPrice(80.0f);
        product.setSellPrice(0.0f);
        product.setStatus("AVAILABLE");
        product.setCategory("Audio");
        product.setSeller(user);
        product = productRepository.save(product);
        Auction auction = new Auction();
        auction.setStartDate(new Date());
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setProduct(product);
        auction.setStatus("ONGOING");
        auction = auctionRepository.save(auction);
        Bid bid = new Bid();
        bid.setValue(90.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(user);
        bid.setProduct(product);
        bid.setAuction(auction);
        bid = bidRepository.save(bid);
        int bidId = bid.getId();
        bidRepository.deleteById(bidId);
        Optional<Bid> deletedBid = bidRepository.findById(bidId);
        assertFalse(deletedBid.isPresent());
    }
}