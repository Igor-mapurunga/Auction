package com.auction.service;

import com.auction.dao.BidRepository;
import com.auction.entities.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BidServiceImpl implements BidService{
    @Autowired
    private BidRepository bidRepository;

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public Bid findById(int theId) {
        Optional<Bid> result = bidRepository.findById(theId);
        Bid theBid = null;
        if (result.isPresent()) {
            theBid = result.get();
        }else {
            throw new RuntimeException("Did not find Bid id - " + theBid);
        }
        return theBid;
    }

    @Override
    public Bid save(Bid theBid) {
        return bidRepository.save(theBid);
    }

    @Override
    public Bid deleteById(int theId) {
         bidRepository.deleteById(theId);
        return null;
    }
}
