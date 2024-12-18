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
    public void updateCustomer(UUID customerId, CustomerDTO customer) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (!customerOpt.isPresent()) {
            return;
        }
        Customer existingCustomer = customerOpt.get();
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerRepository.save(existingCustomer);
    }
    
    @Override
    public void patchCustomer(UUID customerId, CustomerDTO customer) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (!customerOpt.isPresent()) {
            return;
        }
        Customer existingCustomer = customerOpt.get();
        if (customer.getCustomerName() != null) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customerRepository.save(existingCustomer);
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
