package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer saveNewCustomer(Customer customer);

    void updateCustomer(UUID id, Customer customer);

    Customer getCustomer(UUID id);

    List<Customer> listCustomers();
}
