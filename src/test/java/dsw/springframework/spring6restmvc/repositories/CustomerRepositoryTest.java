package dsw.springframework.spring6restmvc.repositories;

import dsw.springframework.spring6restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Test
    public void testCustomerRepository() {
         Customer savedCustomer = customerRepository.save(Customer.builder().customerName("Saved Customer").build());
         assertThat(savedCustomer).isNotNull();
         assertThat(savedCustomer.getId()).isNotNull();
    }
}
