package dsw.springframework.spring6restmvc.controllers;


import dsw.springframework.spring6restmvc.model.Customer;
import dsw.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    
    private CustomerService customerService;
    
    @GetMapping
    public List<Customer> listCustomers(){
        log.info("Getting customers");
        return customerService.listCustomers();
    }
    
    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable("id") UUID id){
        return customerService.getCustomer(id);
    }
}
