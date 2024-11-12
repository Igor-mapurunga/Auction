package com.auction.serviceTest;
import com.auction.dao.AuctionRepository;
import com.auction.dto.AuctionDTO;
import com.auction.entities.Auction;
import com.auction.entities.Product;
import com.auction.exception.AuctionStartDatePassedException;
import com.auction.exception.DateValidationException;
import com.auction.exception.ProductNotFoundException;
import com.auction.exception.ResourceNotFoundException;
import com.auction.service.AuctionServiceImpl;
import com.auction.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById_AuctionExists() {
        Auction auction = new Auction();
        auction.setId(1);
        when(auctionRepository.findById(1)).thenReturn(Optional.of(auction));

        Auction result = auctionService.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(auctionRepository, times(1)).findById(1);
        System.out.println("Auction id was found");
    }

    @Test
    public void testFindById_AuctionDoesNotExist() {
        when(auctionRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> auctionService.findById(2));
        verify(auctionRepository, times(1)).findById(2);
        System.out.println("Auction Id was not found");
    }

    @Test
    public void testCreateAuction_WithProductIdAndAuctionDTO() {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);
        System.out.println("Produto criado");

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setStatus("Open");
        auctionDTO.setStartDate(LocalDateTime.now());
        auctionDTO.setEndDate(LocalDateTime.now().plusDays(1));
        System.out.println("Auction criada e setada");

        Auction expectedAuction = new Auction();
        expectedAuction.setProduct(product);
        expectedAuction.setStatus(auctionDTO.getStatus());
        expectedAuction.setStartDate(Date.from(auctionDTO.getStartDate().atZone(ZoneId.systemDefault()).toInstant()));
        expectedAuction.setEndDate(Date.from(auctionDTO.getEndDate().atZone(ZoneId.systemDefault()).toInstant()));


        when(productService.findById(productId)).thenReturn(product);
        when(auctionRepository.save(any(Auction.class))).thenReturn(expectedAuction);


        Auction createdAuction = auctionService.createAuction(productId, auctionDTO);
        assertNotNull(createdAuction);
        assertEquals(expectedAuction.getProduct(), createdAuction.getProduct());
        assertEquals(expectedAuction.getStatus(), createdAuction.getStatus());
        assertEquals(expectedAuction.getStartDate(), createdAuction.getStartDate());
        assertEquals(expectedAuction.getEndDate(), createdAuction.getEndDate());

        verify(productService, times(1)).findById(productId);
        verify(auctionRepository, times(1)).save(any(Auction.class));
    }
    @Test
    public void testCreateAuction_EndDateBeforeStartDate() {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setStatus("Open");
        auctionDTO.setStartDate(LocalDateTime.now().plusDays(1));
        auctionDTO.setEndDate(LocalDateTime.now());

        when(productService.findById(productId)).thenReturn(product);


        assertThrows(IllegalArgumentException.class, () -> auctionService.createAuction(productId, auctionDTO));


        verify(auctionRepository, never()).save(any(Auction.class));
    }
    @Test
    public void testCreateAuction_ProductDoesNotExist() {
        int productId = 1;

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setStatus("Open");
        auctionDTO.setStartDate(LocalDateTime.now());
        auctionDTO.setEndDate(LocalDateTime.now().plusDays(1));

        when(productService.findById(productId)).thenThrow(new ProductNotFoundException(productId));

        assertThrows(ProductNotFoundException.class, () -> auctionService.createAuction(productId, auctionDTO));

        verify(auctionRepository, never()).save(any(Auction.class));
    }
    @Test
    public void testDeleteAuction_AuctionExistsAndBeforeStartDate() {
        int auctionId = 1;
        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStartDate(new Date(System.currentTimeMillis() + 86400000));

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        auctionService.deleteById(auctionId);

        verify(auctionRepository, times(1)).deleteById(auctionId);
    }

    @Test
    public void testDeleteAuction_AuctionExistsAndAfterStartDate() {
        int auctionId = 1;
        Auction auction = new Auction();
        auction.setId(auctionId);
        auction.setStartDate(new Date(System.currentTimeMillis() - 86400000));

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        assertThrows(AuctionStartDatePassedException.class, () -> auctionService.deleteById(auctionId));

        verify(auctionRepository, never()).deleteById(auctionId);
    }
    @Test
    public void testDeleteAuction_AuctionDoesNotExist() {
        int auctionId = 1;

        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> auctionService.deleteById(auctionId));
        verify(auctionRepository, never()).deleteById(auctionId);
    }
    @Test
    public void testCreateAuction_StartDateEqualsEndDate() {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setStatus("Open");
        auctionDTO.setStartDate(LocalDateTime.now());
        auctionDTO.setEndDate(LocalDateTime.now());

        when(productService.findById(productId)).thenReturn(product);

        assertThrows(DateValidationException.class, () -> auctionService.createAuction(productId, auctionDTO));

        verify(auctionRepository, never()).save(any(Auction.class));
    }
}
