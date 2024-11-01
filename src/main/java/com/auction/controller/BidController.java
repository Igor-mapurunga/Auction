package com.auction.controller;

import com.auction.entities.Bid;
import com.auction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {
    @Autowired
    private BidService bidService;

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
        bidService.createBid(userId, auctionId, bidRequest);
        return ResponseEntity.ok("Bid placed successfully");
    }
}
