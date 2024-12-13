package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private Map<UUID, Customer> customers;
    
    public CustomerServiceImpl() {
        customers = new HashMap<>();
        Customer customer1 = Customer.builder()
                .customerName("Max Mustermann")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        Customer customer2 = Customer.builder()
                .customerName("Hans Fritz")
                .id(UUID.randomUUID())
                .version(2)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        Customer customer3 = Customer.builder()
                .customerName("Jochen Schmulzer")
                .id(UUID.randomUUID())
                .version(5)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        customers.put(customer1.getId(), customer1);
        customers.put(customer2.getId(), customer2);
        customers.put(customer3.getId(), customer3);
    }
    
    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer newCustomer = Customer.builder()
                .customerName(customer.getCustomerName())
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        customers.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }
    
    @Override
    public void updateCustomer(UUID id, Customer customer) {
        Customer existingCustomer = customers.get(id);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customers.put(id, existingCustomer);
    }
    
    @Override
    public void patchCustomer(UUID id, Customer customer) {
        Customer existingCustomer = customers.get(id);
        if (existingCustomer == null) {
            return;
        }
        if (customer.getCustomerName() != null) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customers.put(id, existingCustomer);
    }
    
    @Override
    public Customer getCustomer(UUID id) {
        return customers.get(id);
    }
    
    @Override
    public List<Customer> listCustomers() {
        return customers.values().stream().toList();
    }
    
    @Override
    public void deleteCustomerById(UUID customerId) {
        customers.remove(customerId);
    }
}
