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
        return List.copyOf(beerMap.values());
    }
    
    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return beerMap.get(id);
    }
    
}
