package dsw.springframework.spring6restmvc.repositories;

import dsw.springframework.spring6restmvc.entities.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BeerRepositoryTest {
    
    @Autowired
    BeerRepository beerRepository;
    
    @Test
    public void testBeerRepository() {
        Beer savedBeer = beerRepository.save(Beer.builder().beerName("Saved Beer").build());
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
        
    }
}
