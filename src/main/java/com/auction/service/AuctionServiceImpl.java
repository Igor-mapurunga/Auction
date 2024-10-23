package com.auction.service;

import com.auction.dao.AuctionRepository;
import com.auction.entities.Auction;
import com.auction.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImpl implements AuctionService {
    @Autowired
    private AuctionRepository auctionRepository;
    @Override
    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }
    @Override
    public Auction findById(int theId) {
        Optional<Auction> result = auctionRepository.findById(theId);
        return result.orElseThrow(() -> new ResourceNotFoundException("Auction not found with id - " + theId));
    }
    @Override
    public Auction save(Auction auction) {
        return auctionRepository.save(auction);
    }
    @Override
    public Auction deleteById(int theId) {
        Auction auction = findById(theId);
        ZonedDateTime startDateTime = ZonedDateTime.ofInstant(
                auction.getStartDate().toInstant(),
                ZoneId.of("America/Sao_Paulo")
        );
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        if (now.isBefore(startDateTime)) {
            auctionRepository.deleteById(theId);
        } else {
            throw new IllegalArgumentException("Cannot delete the auction. The start date and time have already passed.");
        }
        return auction;
    }
}
