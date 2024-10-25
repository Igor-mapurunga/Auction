package com.auction.service;

import com.auction.dao.BidRepository;
import com.auction.entities.Auction;
import com.auction.entities.Bid;
import com.auction.entities.Product;
import com.auction.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


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
                .orElseThrow(() -> new RuntimeException("Did not find Bid id - " + theId));
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
        Auction auction = auctionService.findById(auctionId);

        if (user == null) {
            return "User not found with ID: " + userId;
        } else if (auction == null) {
            return "Auction not found with ID: " + auctionId;
        }

        Date date = new Date();
        if (date.before(auction.getStartDate()) || date.after(auction.getEndDate())) {
            return "Cannot place a bid, the auction is not active";
        }

        Product product = auction.getProduct();
        if (product == null) {
            return "Auction does not have a valid product associated.";
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

