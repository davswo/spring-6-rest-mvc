package dsw.springframework.spring6restmvc.controllers;

import dsw.springframework.spring6restmvc.model.Beer;
import dsw.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    
    private final BeerService beerService;

    @GetMapping()
    public List<Beer> listAllBeers() {
        return beerService.listBeers();
    }
    
    @GetMapping("{id}")
    public Beer getBeerById(@PathVariable("id") UUID id){
        log.debug("Get Beer by Id - in controller");
        return beerService.getBeerById(id);
    }

}
