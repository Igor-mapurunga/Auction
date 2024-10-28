package com.auction.controller;

import com.auction.dto.AuctionDTO;
import com.auction.entities.Auction;
import com.auction.exception.ResourceNotFoundException;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping()
    public List<Auction> getAllAuctions() {
        return auctionService.findAll();
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable int auctionId) {
        Auction auction = auctionService.findById(auctionId);
        return auction != null ? ResponseEntity.ok(auction) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/addNewAuction/{productId}")
    public ResponseEntity<Auction> addNewAuction(@PathVariable int productId, @RequestBody AuctionDTO auctionDTO) {
        Auction auction = auctionService.createAuction(productId, auctionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(auction);
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable int auctionId) {
        try {
            auctionService.deleteById(auctionId);
            return ResponseEntity.ok("Auction deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


