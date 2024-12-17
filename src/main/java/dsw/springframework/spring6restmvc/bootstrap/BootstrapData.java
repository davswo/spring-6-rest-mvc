package dsw.springframework.spring6restmvc.bootstrap;

import dsw.springframework.spring6restmvc.entities.Beer;
import dsw.springframework.spring6restmvc.entities.Customer;
import dsw.springframework.spring6restmvc.model.BeerStyle;
import dsw.springframework.spring6restmvc.repositories.BeerRepository;
import dsw.springframework.spring6restmvc.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    public BootstrapData(BeerRepository beerRepository, CustomerRepository customerRepository) {
        this.beerRepository = beerRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        Customer customer1 = Customer.builder()
                .customerName("Max Mustermann")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .customerName("Hans Fritz")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .customerName("Jochen Schmulzer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);

        Beer beer1 = Beer.builder()
                .beerName("Camba Chiemsee Pale")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("2.21"))
                .quantityOnHand(21)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Camba Chiemsee Dunkel")
                .beerStyle(BeerStyle.DUNKEL)
                .upc("12357")
                .price(new BigDecimal("2.22"))
                .quantityOnHand(22)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .beerName("Camba Chiemsee Helles")
                .beerStyle(BeerStyle.HELLES)
                .upc("12358")
                .price(new BigDecimal("2.23"))
                .quantityOnHand(23)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        
        beerRepository.save(beer1);
        beerRepository.save(beer2);
        beerRepository.save(beer3);
    }
}