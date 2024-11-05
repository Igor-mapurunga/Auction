package com.auction;

import com.auction.controller.BidController;
import com.auction.entities.Bid;
import com.auction.exception.BidNotFoundException;
import com.auction.exception.GlobalExceptionHandler;
import com.auction.service.AuctionService;
import com.auction.service.BidService;
import com.auction.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidController.class)
@Import(GlobalExceptionHandler.class)
public class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidService bidService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuctionService auctionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllBids() throws Exception {
        when(bidService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bids"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(bidService, times(1)).findAll();
    }

    @Test
    public void testGetBidById() throws Exception {
        Bid bid = new Bid();
        bid.setId(1);
        when(bidService.findById(1)).thenReturn(bid);

        mockMvc.perform(get("/bids/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bidService, times(1)).findById(1);
    }

    @Test
    public void testGetBidById_NotFound() throws Exception {
        when(bidService.findById(1)).thenThrow(new BidNotFoundException(1));

        mockMvc.perform(get("/bids/1"))
                .andExpect(status().isInternalServerError());

        verify(bidService, times(1)).findById(1);
    }

    @Test
    public void testCreateBid_Success() throws Exception {
        int userId = 1;
        int auctionId = 1;
        Bid bidRequest = new Bid();
        bidRequest.setValue(100);

        when(bidService.createBid(eq(userId), eq(auctionId), any(Bid.class)))
                .thenReturn("Bid placed successfully");

        mockMvc.perform(post("/bids/{userId}/{auctionId}", userId, auctionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bidRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bid placed successfully"));


        verify(bidService, times(1)).createBid(eq(userId), eq(auctionId), any(Bid.class));
    }
}