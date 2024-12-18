package dsw.springframework.spring6restmvc.controller;

import dsw.springframework.spring6restmvc.controllers.CustomerController;
import dsw.springframework.spring6restmvc.entities.Customer;
import dsw.springframework.spring6restmvc.exceptions.NotFoundException;
import dsw.springframework.spring6restmvc.model.CustomerDTO;
import dsw.springframework.spring6restmvc.repositories.CustomerRepository;
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
public class CustomerControllerIT {
    
    @Autowired
    CustomerController customerController;
    
    @Autowired
    CustomerRepository customerRepository;
    
    @Test
    public void testNotFoundException() {
        assertThrows(NotFoundException.class, () ->
                customerController.getCustomerById(UUID.randomUUID())
        );
    }
    
    @Test
    public void testGetCustomerById() {
        final Customer customer = customerRepository.findAll().get(0);
        final CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        
        assertThat(customerDTO).isNotNull();
    }
    
    @Test
    public void testListAllCustomers() {
        final List<CustomerDTO> customerDTOS = customerController.listAllCustomers();
        
        assertThat(customerDTOS).hasSize(3);
    }
    
    @Rollback
    @Transactional
    @Test
    public void testEmptyList() {
        customerRepository.deleteAll();
        
        final List<CustomerDTO> customerDTOS = customerController.listAllCustomers();
        
        assertThat(customerDTOS).hasSize(0);
    }
}
