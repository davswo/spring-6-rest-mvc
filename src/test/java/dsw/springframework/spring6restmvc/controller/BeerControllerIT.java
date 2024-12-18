package dsw.springframework.spring6restmvc.controller;

import dsw.springframework.spring6restmvc.controllers.BeerController;
import dsw.springframework.spring6restmvc.entities.Beer;
import dsw.springframework.spring6restmvc.exceptions.NotFoundException;
import dsw.springframework.spring6restmvc.mappers.BeerMapper;
import dsw.springframework.spring6restmvc.model.BeerDTO;
import dsw.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BeerControllerIT {
    
    @Autowired
    BeerController beerController;
    
    @Autowired
    BeerRepository beerRepository;
    
    @Autowired
    BeerMapper beerMapper;
    
    @Test
    public void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> 
                beerController.getBeerById(UUID.randomUUID())
        );
    }
    
    @Test
    public void testGetBeerById() {
        final Beer beer = beerRepository.findAll().get(0);
        final BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        
        assertThat(beerDTO).isNotNull();
    }
    
    @Test
    public void testListAllBeers() {
        final List<BeerDTO> beerDTOS = beerController.listAllBeers();
        
        assertThat(beerDTOS).hasSize(3);
    }
    
    @Rollback
    @Transactional
    @Test
    public void testEmptyList() {
        beerRepository.deleteAll();
        
        final List<BeerDTO> beerDTOS = beerController.listAllBeers();
        
        assertThat(beerDTOS).hasSize(0);
    }
    
    @Rollback
    @Transactional
    @Test
    public void testSaveNewBeer() {
        final BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();

        final ResponseEntity responseEntity = beerController.saveNewBeer(beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        
        String[] locationPath = responseEntity.getHeaders().getLocation().getPath().split("/");
        final UUID beerId = UUID.fromString(locationPath[locationPath.length - 1]);

        final Optional<Beer> newBeerOpt = beerRepository.findById(beerId);
        assertThat(newBeerOpt).isPresent();
        assertThat(newBeerOpt.get().getBeerName()).isEqualTo("New Beer");
    }
    
    @Rollback
    @Transactional
    @Test
    public void testUpdateBeerById() {
        final Beer beer = beerRepository.findAll().get(0);
        final BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        
        String newBeerName = "UPDATED";
        
        beerDTO.setBeerName(newBeerName);

        final ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        
        final Optional<Beer> updatedBeerOpt = beerRepository.findById(beer.getId());
        assertThat(updatedBeerOpt).isPresent();
        assertThat(updatedBeerOpt.get().getBeerName()).isEqualTo(newBeerName);
    }
    
    @Test
    public void testUpdateNotFound() {
        final BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();
        
        assertThrows(NotFoundException.class, () -> 
                beerController.updateBeerById(UUID.randomUUID(), beerDTO)
        );
    }
}
