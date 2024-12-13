package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.Beer;
import dsw.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    
    private Map<UUID, Beer> beerMap;
    
    public BeerServiceImpl() {
        beerMap = new HashMap<>();
        
        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Camba Chiemsee Pale")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("2.21"))
                .quantityOnHand(21)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Camba Chiemsee Dunkel")
                .beerStyle(BeerStyle.DUNKEL)
                .upc("12357")
                .price(new BigDecimal("2.22"))
                .quantityOnHand(22)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Camba Chiemsee Helles")
                .beerStyle(BeerStyle.HELLES)
                .upc("12358")
                .price(new BigDecimal("2.23"))
                .quantityOnHand(23)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }
    
    @Override
    public List<Beer> listBeers() {
        return beerMap.values().stream().toList();
    }
    
    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return beerMap.get(id);
    }
    
    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer newBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }
    
    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdateDate(LocalDateTime.now());
        beerMap.put(beerId, existingBeer);
    }
    
    @Override
    public void patchBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        if (existingBeer == null) {
            return;
        }
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
        beerMap.put(beerId, existingBeer);
    }
    
    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);
    }
        
}
