package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.entities.Beer;
import dsw.springframework.spring6restmvc.mappers.BeerMapper;
import dsw.springframework.spring6restmvc.model.BeerDTO;
import dsw.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BeerServiceImpl implements BeerService {
    
    private BeerRepository beerRepository;
    private BeerMapper beerMapper;
    
    @Override
    public List<BeerDTO> getAllBeers() {
        return beerRepository.findAll().stream().map(beerMapper::beerToBeerDto).toList();
    }
    
    @Override
    public Optional<BeerDTO> getBeerById(UUID beerId) {
        log.debug("Get Beer by Id - in service. Id: " + beerId.toString());
        final Optional<Beer> beerByIdOpt = beerRepository.findById(beerId);
        if (beerByIdOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(beerMapper.beerToBeerDto(beerByIdOpt.get()));
    }
    
    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        beer.setCreatedDate(LocalDateTime.now());
        beer.setUpdateDate(LocalDateTime.now());
        final Beer newBeer = beerRepository.save(beerMapper.beerDtoToBeer(beer));
        return beerMapper.beerToBeerDto(newBeer);
    }
    
    @Override
    public void updateBeerById(UUID beerId, BeerDTO beer) {
        Optional<Beer> beerOpt = beerRepository.findById(beerId);
        if (!beerOpt.isPresent()) {
            return;
        }
        Beer existingBeer = beerOpt.get();
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdateDate(LocalDateTime.now());
        beerRepository.save(existingBeer);
    }
    
    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {
        Optional<Beer> beerOpt = beerRepository.findById(beerId);
        if (!beerOpt.isPresent()) {
            return;
        }
        Beer existingBeer = beerOpt.get();
        if (beer.getBeerName() != null) {
            existingBeer.setBeerName(beer.getBeerName());
        }
        if (beer.getBeerStyle() != null) {
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }
        if (beer.getUpc() != null) {
            existingBeer.setUpc(beer.getUpc());
        }
        if (beer.getPrice() != null) {
            existingBeer.setPrice(beer.getPrice());
        }
        if (beer.getQuantityOnHand() != null) {
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        }
        existingBeer.setVersion(existingBeer.getVersion() + 1);
        existingBeer.setUpdateDate(LocalDateTime.now());
        beerRepository.save(existingBeer);
    }
    
    @Override
    public void deleteBeerById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }
        
}
