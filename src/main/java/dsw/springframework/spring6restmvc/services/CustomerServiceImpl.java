package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    
    private Map<UUID, CustomerDTO> customerMap;
    
    public CustomerServiceImpl() {
        customerMap = new HashMap<>();
        CustomerDTO customer1 = CustomerDTO.builder()
                .customerName("Max Mustermann")
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        CustomerDTO customer2 = CustomerDTO.builder()
                .customerName("Hans Fritz")
                .id(UUID.randomUUID())
                .version(2)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        
        CustomerDTO customer3 = CustomerDTO.builder()
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
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO newCustomer = CustomerDTO.builder()
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
    public void updateCustomer(UUID customerId, CustomerDTO customer) {
        Optional<CustomerDTO> customerOpt = getCustomerById(customerId);
        if (!customerOpt.isPresent()) {
            return;
        }
        CustomerDTO existingCustomer = customerOpt.get();
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(customer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);
    }
    
    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {
        Optional<CustomerDTO> customerOpt = getCustomerById(customerId);
        if (!customerOpt.isPresent()) {
            return;
        }
        CustomerDTO existingCustomer = customerOpt.get();
        if (customer.getCustomerName() != null) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerMap.put(customerId, existingCustomer);
    }
    
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.of(customerMap.get(customerId));
    }
    
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerMap.values().stream().toList();
    }
    
    @Override
    public void deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);
    }
    
}
