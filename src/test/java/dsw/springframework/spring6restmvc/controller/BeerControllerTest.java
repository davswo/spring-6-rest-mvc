package dsw.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsw.springframework.spring6restmvc.controllers.BeerController;
import dsw.springframework.spring6restmvc.model.BeerDTO;
import dsw.springframework.spring6restmvc.model.BeerStyle;
import dsw.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> idCaptor;
    
    @Captor
    ArgumentCaptor<BeerDTO> beerCaptor;
    
    private BeerDTO beer1;
    private BeerDTO beer2;
    private BeerDTO beer3;

    @BeforeEach
    void setUp() {
        beer1 = BeerDTO.builder().id(UUID.randomUUID()).beerName("Beer 1").build();
        beer2 = BeerDTO.builder().id(UUID.randomUUID()).beerName("Beer 2").build();
        beer3 = BeerDTO.builder().id(UUID.randomUUID()).beerName("Beer 3").build();
        given(beerService.getAllBeers()).willReturn(List.of(beer1, beer2, beer3));
    }


    @Test
    void testCreateNewBeer() throws Exception {
        UUID id = UUID.randomUUID();
        BeerDTO beer = BeerDTO.builder()
                .id(id)
                .beerName("Test Beer")
                .beerStyle(BeerStyle.HELLES)
                .upc("111")
                .price(BigDecimal.valueOf(2.22))
                .quantityOnHand(2)
                .build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beer);

        System.out.println(objectMapper.writeValueAsBytes(beer));
        System.out.println(objectMapper.writeValueAsString(beer));
        mockMvc.perform(post(BeerController.BEER_PATH)
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BeerController.BEER_PATH + "/" + id));
    }

    @Test
    void testUpdateBeer() throws Exception {
        given(beerService.updateBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer1));
        
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer1.getId())
                        .content(objectMapper.writeValueAsString(beer1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(beerService).updateBeerById(idCaptor.capture(), any(BeerDTO.class));

        assertThat(beer1.getId()).isEqualTo(idCaptor.getValue());
    }

    @Test
    void testCreateBeerNullBeerName() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
    
    @Test
    void testPatchBeer() throws Exception {
        
        given(beerService.patchBeerById(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beer1));
        
        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer1)))
                .andExpect(status().isNoContent());
        
        verify(beerService).patchBeerById(idCaptor.capture(), beerCaptor.capture());
        
        assertThat(beer1.getId()).isEqualTo(idCaptor.getValue());
        assertThat(beer1.getBeerName()).isEqualTo(beerCaptor.getValue().getBeerName());
    }

    @Test
    void testDeleteBeer() throws Exception {
        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
        verify(beerService).deleteBeerById(idCaptor.capture());
        assertThat(beer1.getId()).isEqualTo(idCaptor.getValue());
    }
    
    @Test
    void testGetAllBeers() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetBeerById() throws Exception {

        given(beerService.getBeerById(beer1.getId())).willReturn(Optional.of(beer1));

        mockMvc.perform(get(BeerController.BEER_PATH_ID, beer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beer1.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer1.getBeerName())));
    }
    
    @Test
    void testGetBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))   
                .andExpect(status().isNotFound());
    }
    
}