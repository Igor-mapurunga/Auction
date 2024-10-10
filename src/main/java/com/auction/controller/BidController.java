package com.auction.controller;

import com.auction.entities.Auction;
import com.auction.entities.Bid;
import com.auction.entities.Product;
import com.auction.entities.User;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuctionService auctionService;

    @GetMapping()
    public List<Bid> getAllBids() {
        return bidService.findAll();
    }
    @GetMapping("/{bidId}")
    public Bid getBidById(@PathVariable int bidId) {
        return bidService.findById(bidId);
    }

    @PostMapping("/{userId}/{auctionId}")
    public ResponseEntity<String> createBid( @PathVariable int userId, @PathVariable int auctionId, @RequestBody Bid bidRequest){
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
        Auction auction = auctionService.findById(auctionId);
        if (auction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auction not found with ID: " + auctionId);
        }
        Date date = new Date();
        if (date.before(auction.getStartDate()) || date.after(auction.getEndDate())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot place a bid, the auction is not active");
        }
        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setValue(bidRequest.getValue());
        bid.setTimeStamp(new Date());

        Product product = auction.getProduct();
        if (product == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Auction does not have a valid product associated.");
        }
        bid.setProduct(product);
        bidService.save(bid);

        return ResponseEntity.ok("Bid placed successfully");
    }


}
