package com.auction;
import com.auction.dao.BidRepository;
import com.auction.entities.Auction;
import com.auction.entities.Bid;
import com.auction.entities.Product;
import com.auction.entities.User;
import com.auction.exception.*;
import com.auction.service.AuctionService;
import com.auction.service.BidServiceImpl;
import com.auction.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BidServiceTest {
    @Mock
    private BidRepository bidRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuctionService auctionService;

    @InjectMocks
    private BidServiceImpl bidService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBid_UserNotFound() {
        int userId = 1;
        int auctionId = 2;
        Bid bidRequest = new Bid();

        when(userService.findById(userId)).thenReturn(null);

        RuntimeException thrown = assertThrows(UserNotFoundException.class, () -> {
            bidService.createBid(userId, auctionId, bidRequest);
        });

        assertEquals("Did not find user id - " + userId, thrown.getMessage());
        verify(userService, times(1)).findById(userId);
        verify(auctionService, never()).findById(anyInt());
        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    public void testCreateBid_AuctionNotFound() {
        int userId = 1;
        int auctionId = 2;
        Bid bidRequest = new Bid();
        User user = new User();

        when(userService.findById(userId)).thenReturn(user);
        when(auctionService.findById(auctionId)).thenReturn(null);

        AuctionNotFoundException thrown = assertThrows(AuctionNotFoundException.class, () -> {
            bidService.createBid(userId, auctionId, bidRequest);
        });

        assertEquals("Auction not found with id - " + auctionId, thrown.getMessage());
        verify(userService, times(1)).findById(userId);
        verify(auctionService, times(1)).findById(auctionId);
        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    public void testCreateBid_AuctionNotActive() {
        int userId = 1;
        int auctionId = 2;
        Bid bidRequest = new Bid();
        User user = new User();
        Auction auction = new Auction();
        auction.setStartDate(new Date(System.currentTimeMillis() + 86400000));
        auction.setEndDate(new Date(System.currentTimeMillis() + 172800000));

        when(userService.findById(userId)).thenReturn(user);
        when(auctionService.findById(auctionId)).thenReturn(auction);

        BidNotAllowedException thrown = assertThrows(BidNotAllowedException.class, () -> {
            bidService.createBid(userId, auctionId, bidRequest);
        });

        assertEquals("Cannot place a bid, the auction is not active.", thrown.getMessage());
        verify(userService, times(1)).findById(userId);
        verify(auctionService, times(1)).findById(auctionId);
        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    public void testCreateBid_AuctionHasNoProduct() {
        int userId = 1;
        int auctionId = 2;
        Bid bidRequest = new Bid();
        User user = new User();
        Auction auction = new Auction();
        auction.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));

        when(userService.findById(userId)).thenReturn(user);
        when(auctionService.findById(auctionId)).thenReturn(auction);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            bidService.createBid(userId, auctionId, bidRequest);
        });

        assertEquals("Auction does not have a valid product associated.", thrown.getMessage());
        verify(userService, times(1)).findById(userId);
        verify(auctionService, times(1)).findById(auctionId);
        verify(bidRepository, never()).save(any(Bid.class));
    }

    @Test
    public void testCreateBid_Successful() {
        int userId = 1;
        int auctionId = 2;
        Bid bidRequest = new Bid();
        bidRequest.setValue(100);
        User user = new User();
        Auction auction = new Auction();
        auction.setStartDate(new Date(System.currentTimeMillis() - 86400000));
        auction.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        Product product = new Product();
        auction.setProduct(product);

        when(userService.findById(userId)).thenReturn(user);
        when(auctionService.findById(auctionId)).thenReturn(auction);
        when(bidRepository.save(any(Bid.class))).thenReturn(new Bid());

        String result = bidService.createBid(userId, auctionId, bidRequest);

        assertEquals("Bid placed successfully", result);
        verify(userService, times(1)).findById(userId);
        verify(auctionService, times(1)).findById(auctionId);
        verify(bidRepository, times(1)).save(any(Bid.class));
    }

    @Test
    public void testFindById_BidExists() {
        int bidId = 1;
        Bid bid = new Bid();
        bid.setId(bidId);
        when(bidRepository.findById(bidId)).thenReturn(Optional.of(bid));

        Bid result = bidService.findById(bidId);

        assertEquals(bidId, result.getId());
        verify(bidRepository, times(1)).findById(bidId);
    }

    @Test
    public void testFindById_BidDoesNotExist() {
        int bidId = 1;
        when(bidRepository.findById(bidId)).thenReturn(Optional.empty());

        try {
            bidService.findById(bidId);
        } catch (RuntimeException e) {
            assertEquals("Did not find Bid id - " + bidId, e.getMessage());
        }

        verify(bidRepository, times(1)).findById(bidId);
    }

    @Test
    public void testDeleteById() {
        int bidId = 1;
        doNothing().when(bidRepository).deleteById(bidId);

        bidService.deleteById(bidId);

        verify(bidRepository, times(1)).deleteById(bidId);
    }
}

