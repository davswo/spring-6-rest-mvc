package dsw.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsw.springframework.spring6restmvc.controllers.BeerController;
import dsw.springframework.spring6restmvc.model.Beer;
import dsw.springframework.spring6restmvc.model.BeerStyle;
import dsw.springframework.spring6restmvc.services.BeerService;
import dsw.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testCreateNewBeer() throws Exception {
        UUID id = UUID.randomUUID();
        Beer beer = Beer.builder()
                .id(id)
                .beerName("Test Beer")
                .beerStyle(BeerStyle.HELLES)
                .upc("111")
                .price(BigDecimal.valueOf(2.22))
                .quantityOnHand(2)
                .build();

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beer);

        System.out.println(objectMapper.writeValueAsBytes(beer));
        System.out.println(objectMapper.writeValueAsString(beer));
        mockMvc.perform(post("/api/v1/beer")
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/beer/" + id.toString()));
    }

    @Test
    void testUpdateBeer() throws Exception {
        Beer beer = beerServiceImpl.getAllBeers().get(0);

        mockMvc.perform(put("/api/v1/beer/" + beer.getId())
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).updateBeerById(idCaptor.capture(), any(Beer.class));

        assertThat(beer.getId()).isEqualTo(idCaptor.getValue());
    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().get(0);

        mockMvc.perform(delete("/api/v1/beer/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeerById(idCaptor.capture());
        assertThat(testBeer.getId()).isEqualTo(idCaptor.getValue());
    }
    
    @Test
    void testGetAllBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(beerServiceImpl.getAllBeers());

        mockMvc.perform(get("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
}