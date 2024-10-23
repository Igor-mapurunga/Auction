package com.auction.controller;

import com.auction.entities.Auction;
import com.auction.entities.Product;
import com.auction.exception.ResourceNotFoundException;
import com.auction.service.AuctionService;
import com.auction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private ProductService productService;

    @GetMapping()
    public List<Auction> getALlAuctions() {
        return auctionService.findAll();
    }

    @GetMapping("/{auctionId}")
    public ResponseEntity<Auction> getAuctionById(@PathVariable int auctionId) {
        Auction auction = auctionService.findById(auctionId);
        return auction != null ? ResponseEntity.ok(auction) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/addNewAuction")
    public Auction addNewAuction(
            @RequestParam("productId") int productId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("status") String status
    ) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id - " + productId);
        }
        Auction newAuction = new Auction();
        newAuction.setProduct(product);
        newAuction.setStartDate(Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant()));
        newAuction.setEndDate(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));
        newAuction.setStatus(status);
        return auctionService.save(newAuction);
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

