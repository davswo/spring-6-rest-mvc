package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    CustomerDTO saveNewCustomer(CustomerDTO customer);

    void updateCustomer(UUID id, CustomerDTO customer);

    void patchCustomer(UUID id, CustomerDTO customer);

    Optional<CustomerDTO> getCustomerById(UUID id);

    List<CustomerDTO> getAllCustomers();

    void deleteCustomerById(UUID customerId);
}
