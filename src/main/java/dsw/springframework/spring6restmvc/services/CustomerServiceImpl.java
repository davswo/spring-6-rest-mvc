package dsw.springframework.spring6restmvc.services;

import dsw.springframework.spring6restmvc.entities.Customer;
import dsw.springframework.spring6restmvc.mappers.CustomerMapper;
import dsw.springframework.spring6restmvc.model.CustomerDTO;
import dsw.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
 
    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        customer.setCreatedDate(LocalDateTime.now());
        customer.setLastModifiedDate(LocalDateTime.now());
        Customer newCustomer = customerMapper.customerDtoToCustomer(customer);
        final Customer savedCustomer = customerRepository.save(newCustomer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }
    
    @Override
    public Optional<CustomerDTO> updateCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(existingCustomer -> {
            existingCustomer.setCustomerName(customer.getCustomerName());
            existingCustomer.setLastModifiedDate(LocalDateTime.now());
            customerRepository.save(existingCustomer);
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(existingCustomer)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
    
    @Override
    public Optional<CustomerDTO> patchCustomer(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();
        customerRepository.findById(customerId).ifPresentOrElse(existingCustomer -> {
            if (customer.getCustomerName() != null) {
                existingCustomer.setCustomerName(customer.getCustomerName());
            }
            existingCustomer.setLastModifiedDate(LocalDateTime.now());
            customerRepository.save(existingCustomer);
            atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(existingCustomer)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
    
    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.findById(customerId).orElse(null)));
    }
    
    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).toList();
    }
    
    @Override
    public void deleteCustomerById(UUID customerId) {
        customerRepository.deleteById(customerId);
    }
    
}
