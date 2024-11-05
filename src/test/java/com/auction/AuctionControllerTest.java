package com.auction;
import com.auction.controller.AuctionController;
import com.auction.dto.AuctionDTO;
import com.auction.entities.Auction;
import com.auction.exception.AuctionNotFoundException;
import com.auction.service.AuctionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionController.class)
public class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuctionService auctionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllAuctions() throws Exception {
        when(auctionService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/auctions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(auctionService, times(1)).findAll();
    }

    @Test
    public void testGetAuctionById() throws Exception {
        Auction auction = new Auction();
        auction.setId(1);
        when(auctionService.findById(1)).thenReturn(auction);

        mockMvc.perform(get("/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(auctionService, times(1)).findById(1);
    }

    @Test
    public void testGetAuctionById_AuctionNotFound() throws Exception {
        when(auctionService.findById(1)).thenThrow(new AuctionNotFoundException(1));

        mockMvc.perform(get("/auctions/1"))
                .andExpect(status().isNotFound());

        verify(auctionService, times(1)).findById(1);
    }

    @Test
    public void testAddNewAuction_ValidData() throws Exception {
        int productId = 1;
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setStatus("Open");
        auctionDTO.setStartDate(LocalDateTime.now().plusDays(1));
        auctionDTO.setEndDate(LocalDateTime.now().plusDays(2));

        Auction auction = new Auction();
        auction.setId(1);
        when(auctionService.createAuction(eq(productId), any(AuctionDTO.class))).thenReturn(auction);

        mockMvc.perform(post("/auctions/addNewAuction/{productId}", productId).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auctionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(auctionService, times(1)).createAuction(eq(productId), any(AuctionDTO.class));
    }

    @Test
    public void testDeleteAuction_AuctionExistsAndBeforeStartDate() throws Exception {
        int auctionId = 1;

        doNothing().when(auctionService).deleteById(auctionId);

        mockMvc.perform(delete("/auctions/{auctionId}", auctionId)).andExpect(status().isOk())
                .andExpect(content().string("Auction deleted successfully."));

        verify(auctionService, times(1)).deleteById(auctionId);
    }

    @Test
    public void testDeleteAuction_AuctionDoesNotExist() throws Exception {
        int auctionId = 1;

        doThrow(new AuctionNotFoundException(auctionId)).when(auctionService).deleteById(auctionId);

        mockMvc.perform(delete("/auctions/{auctionId}", auctionId)).andExpect(status().isNotFound());

        verify(auctionService, times(1)).deleteById(auctionId);
    }
}
