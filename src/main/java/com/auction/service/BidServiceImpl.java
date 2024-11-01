package com.auction.service;

import com.auction.dao.BidRepository;
import com.auction.entities.Auction;
import com.auction.entities.Bid;
import com.auction.entities.Product;
import com.auction.entities.User;
import com.auction.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;



@Service
public class BidServiceImpl implements BidService{
    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuctionService auctionService;

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public Bid findById(int theId) {
        return bidRepository.findById(theId)
                .orElseThrow(() -> new BidNotFoundException(theId));
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

    @Override
    public String createBid(int userId, int auctionId, Bid bidRequest) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }

        Auction auction = auctionService.findById(auctionId);
        if (auction == null) {
            throw new AuctionNotFoundException(auctionId);
        }

        Date date = new Date();
        if (date.before(auction.getStartDate()) || date.after(auction.getEndDate())) {
            throw new BidNotAllowedException("Cannot place a bid, the auction is not active.");
        }

        Product product = auction.getProduct();
        if (product == null) {
            throw new ResourceNotFoundException("Auction does not have a valid product associated.");
        }

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setAuction(auction);
        bid.setValue(bidRequest.getValue());
        bid.setTimeStamp(new Date());
        bid.setProduct(product);
        bidRepository.save(bid);

        return "Bid placed successfully";
    }
}

