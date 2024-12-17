package dsw.springframework.spring6restmvc.mappers;

import dsw.springframework.spring6restmvc.entities.Customer;
import dsw.springframework.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;


@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);

}