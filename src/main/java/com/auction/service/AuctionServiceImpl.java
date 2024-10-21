package com.auction.service;

import com.auction.dao.AuctionRepository;
import com.auction.entities.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Auction theAuction = null;
        if(result.isPresent()) {
            theAuction = result.get();
        }else {
            throw new RuntimeException("Did not find auction id - " + theId);
        }
        return theAuction;
    }

    @Override
    public Auction save(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Override
    public Auction deleteById(int theId) {
        Auction auction = findById(theId);
         auctionRepository.deleteById(theId);
        return auction;
    }
}
