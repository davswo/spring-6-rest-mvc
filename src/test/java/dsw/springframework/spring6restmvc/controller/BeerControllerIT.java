package dsw.springframework.spring6restmvc.controller;

import dsw.springframework.spring6restmvc.controllers.BeerController;
import dsw.springframework.spring6restmvc.entities.Beer;
import dsw.springframework.spring6restmvc.exceptions.NotFoundException;
import dsw.springframework.spring6restmvc.model.BeerDTO;
import dsw.springframework.spring6restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BeerControllerIT {
    
    @Autowired
    BeerController beerController;
    
    @Autowired
    BeerRepository beerRepository;
    
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
}
