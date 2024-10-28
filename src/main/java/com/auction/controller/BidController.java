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
    public ResponseEntity<String> createBid(@PathVariable int userId, @PathVariable int auctionId, @RequestBody Bid bidRequest) {
        String response = bidService.createBid(userId, auctionId, bidRequest);
        return response.equals("Bid placed successfully") ? ResponseEntity.ok(response) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
