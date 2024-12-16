package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.exceptions.NotFoundException;
import dsw.springframework.spring6restmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private Map<UUID, Customer> customerMap;
    
    public CustomerServiceImpl() {
        customerMap = new HashMap<>();
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
        
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
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
        customerMap.put(newCustomer.getId(), newCustomer);
        return newCustomer;
    }
    
    @Override
    public void updateCustomer(UUID customerId, Customer customer) {
        Customer existingCustomer = getCustomerByIdInternal(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);
    }
    
    @Override
    public void patchCustomer(UUID customerId, Customer customer) {
        Customer existingCustomer = getCustomerByIdInternal(customerId);
        if (existingCustomer == null) {
            return;
        }
        if (customer.getCustomerName() != null) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);
    }
    
    @Override
    public Customer getCustomerById(UUID customerId) {
        return getCustomerByIdInternal(customerId);
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        return customerMap.values().stream().toList();
    }
    
    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }
    
    private Customer getCustomerByIdInternal(UUID customerId) {
        final Customer customer = customerMap.get(customerId);
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        return customer;
    }
}
