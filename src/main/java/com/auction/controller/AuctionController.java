package com.auction.controller;

import com.auction.entities.Auction;
import com.auction.entities.Product;
import com.auction.service.AuctionService;
import com.auction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        if (auction != null) {
            return ResponseEntity.ok(auction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{productId}")
    public Auction createAuction(@RequestBody Auction theAuction, @PathVariable int productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new RuntimeException("Product not found with id - " + productId);
        }
        theAuction.setProduct(product);
        return auctionService.save(theAuction);
    }

    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable int auctionId) {
        Auction auction = auctionService.findById(auctionId);
        if (auction != null) {
            ZonedDateTime startDateTime = ZonedDateTime.ofInstant(
                    auction.getStartDate().toInstant(),
                    ZoneId.of("America/Sao_Paulo")
            );
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

            System.out.println("Current time in Sao Paulo: " + now);
            System.out.println("Auction start time in Sao Paulo: " + startDateTime);

            if (now.isBefore(startDateTime)) {
                auctionService.deleteById(auctionId);
                return ResponseEntity.ok("Auction deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Cannot delete the auction. The start date and time have already passed.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auction not found.");
        }
    }
}

