package com.auction.repositoryTest;

import com.auction.dao.AuctionRepository;
import com.auction.dao.BidRepository;
import com.auction.dao.ProductRepository;
import com.auction.dao.UserRepository;
import com.auction.entities.Auction;
import com.auction.entities.Bid;
import com.auction.entities.Product;
import com.auction.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    private User savedUser;
    private Product savedProduct;
    private Auction savedAuction;

    private User getSavedUser() {
        if (savedUser == null) {
            savedUser = createUser("Default Bidder", "password123", "123456789", "123 Main St");
        }
        return savedUser;
    }

    private Product getSavedProduct() {
        if (savedProduct == null) {
            savedProduct = createProduct("Default Product", 100.0f, "AVAILABLE", "Category", getSavedUser());
        }
        return savedProduct;
    }

    private Auction getSavedAuction() {
        if (savedAuction == null) {
            savedAuction = createAuction(getSavedProduct(), new Date(), new Date(System.currentTimeMillis() + 86400000), "ONGOING");
        }
        return savedAuction;
    }

    private User createUser(String name, String password, String phoneNumber, String address) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        return userRepository.save(user);
    }

    private Product createProduct(String name, float startingPrice, String status, String category, User seller) {
        Product product = new Product();
        product.setName(name);
        product.setStartingPrice(startingPrice);
        product.setSellPrice(0.0f);
        product.setStatus(status);
        product.setCategory(category);
        product.setSeller(seller);
        return productRepository.save(product);
    }

    private Auction createAuction(Product product, Date startDate, Date endDate, String status) {
        Auction auction = new Auction();
        auction.setProduct(product);
        auction.setStartDate(startDate);
        auction.setEndDate(endDate);
        auction.setStatus(status);
        return auctionRepository.save(auction);
    }

    @Test
    @Rollback(false)
    public void testSaveBid() {
        Bid bid = new Bid();
        bid.setValue(250.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(getSavedUser());
        bid.setProduct(getSavedProduct());
        bid.setAuction(getSavedAuction());

        Bid savedBid = bidRepository.save(bid);

        assertNotNull(savedBid);
        assertTrue(savedBid.getId() > 0);
        assertEquals(250.0f, savedBid.getValue());
        assertEquals(getSavedUser().getId(), savedBid.getUser().getId());
        assertEquals(getSavedProduct().getId(), savedBid.getProduct().getId());
        assertEquals(getSavedAuction().getId(), savedBid.getAuction().getId());
    }

    @Test
    public void testFindBidById() {
        Bid bid = new Bid();
        bid.setValue(600.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(getSavedUser());
        bid.setProduct(getSavedProduct());
        bid.setAuction(getSavedAuction());

        bid = bidRepository.save(bid);

        Optional<Bid> foundBid = bidRepository.findById(bid.getId());

        assertTrue(foundBid.isPresent());
        assertEquals(600.0f, foundBid.get().getValue());
    }

    @Test
    public void testFindAllBids() {
        Bid bid1 = new Bid();
        bid1.setValue(350.0f);
        bid1.setTimeStamp(new Date());
        bid1.setUser(getSavedUser());
        bid1.setProduct(getSavedProduct());
        bid1.setAuction(getSavedAuction());

        Bid bid2 = new Bid();
        bid2.setValue(400.0f);
        bid2.setTimeStamp(new Date());
        bid2.setUser(getSavedUser());
        bid2.setProduct(getSavedProduct());
        bid2.setAuction(getSavedAuction());

        bidRepository.save(bid1);
        bidRepository.save(bid2);

        List<Bid> bids = bidRepository.findAll();

        assertFalse(bids.isEmpty());
        assertTrue(bids.size() >= 2);
    }

    @Test
    @Rollback(false)
    public void testUpdateBid() {
        Bid bid = new Bid();
        bid.setValue(120.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(getSavedUser());
        bid.setProduct(getSavedProduct());
        bid.setAuction(getSavedAuction());
        bid = bidRepository.save(bid);

        Bid bidToUpdate = bidRepository.findById(bid.getId()).get();
        bidToUpdate.setValue(150.0f);
        Bid updatedBid = bidRepository.save(bidToUpdate);

        assertEquals(150.0f, updatedBid.getValue());
    }

    @Test
    @Rollback(false)
    public void testDeleteBid() {
        Bid bid = new Bid();
        bid.setValue(90.0f);
        bid.setTimeStamp(new Date());
        bid.setUser(getSavedUser());
        bid.setProduct(getSavedProduct());
        bid.setAuction(getSavedAuction());
        bid = bidRepository.save(bid);

        int bidId = bid.getId();
        bidRepository.deleteById(bidId);
        Optional<Bid> deletedBid = bidRepository.findById(bidId);

        assertFalse(deletedBid.isPresent());
    }
}
