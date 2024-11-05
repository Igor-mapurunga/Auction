package com.auction.service;
import com.auction.dao.AuctionRepository;
import com.auction.dto.AuctionDTO;
import com.auction.entities.Auction;
import com.auction.entities.Product;
import com.auction.exception.AuctionNotFoundException;
import com.auction.exception.AuctionStartDatePassedException;
import com.auction.exception.DateValidationException;
import com.auction.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ProductService productService;

    @Override
    public List<Auction> findAll() {
        return auctionRepository.findAll();
    }

    @Override
    public Auction findById(int auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException(auctionId));
    }

    @Override
    public Auction createAuction(int productId, AuctionDTO auctionDTO) {
        Product product = productService.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }

        if (!auctionDTO.getEndDate().isAfter(auctionDTO.getStartDate())) {
            throw new DateValidationException("End date must be after start date.");
        }

        Auction newAuction = new Auction();
        newAuction.setProduct(product);
        newAuction.setStartDate(Date.from(auctionDTO.getStartDate().atZone(ZoneId.systemDefault()).toInstant()));
        newAuction.setEndDate(Date.from(auctionDTO.getEndDate().atZone(ZoneId.systemDefault()).toInstant()));
        newAuction.setStatus(auctionDTO.getStatus());

        return auctionRepository.save(newAuction);
    }
    @Override
    public void deleteById(int auctionId) {
        Auction auction = findById(auctionId);

        Date now = new Date();
        if (now.after(auction.getStartDate())) {
            throw new AuctionStartDatePassedException("Cannot delete the auction. The start date has already passed.");
        }
        auctionRepository.deleteById(auctionId);
    }
}
