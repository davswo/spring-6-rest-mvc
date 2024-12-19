package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> getAllBeers();

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    Optional<BeerDTO>  patchBeerById(UUID beerId, BeerDTO beer);

    void deleteBeerById(UUID beerId);
}
