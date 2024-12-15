package dsw.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsw.springframework.spring6restmvc.controllers.CustomerController;
import dsw.springframework.spring6restmvc.model.Customer;
import dsw.springframework.spring6restmvc.services.CustomerService;
import dsw.springframework.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    @MockitoBean
    CustomerService customerService;

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    @Captor
    ArgumentCaptor<Customer> customerCaptor;
    
    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();
    
    @Test
    void testCreateNewCustomer() throws Exception {
        UUID id = UUID.randomUUID();
        Customer customer = Customer.builder()
                .customerName("Test Customer")
                .id(id)
                .build();


        given(customerService.saveNewCustomer(any(Customer.class))).willReturn(customer);

        System.out.println(objectMapper.writeValueAsBytes(customer));
        System.out.println(objectMapper.writeValueAsString(customer));
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CustomerController.CUSTOMER_PATH + "/" + id));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).updateCustomer(idCaptor.capture(), any(Customer.class));

        assertThat(customer.getId()).isEqualTo(idCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomer(idCaptor.capture(), customerCaptor.capture());

        assertThat(customer.getId()).isEqualTo(idCaptor.getValue());
        assertThat(customer.getCustomerName()).isEqualTo(customerCaptor.getValue().getCustomerName());
    }
    
    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(idCaptor.capture());
        assertThat(customer.getId()).isEqualTo(idCaptor.getValue());
    }
    
    @Test
    void testGetAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void TestGetCustomerById() throws Exception {
        final Customer customer = customerServiceImpl.getAllCustomers().get(0);
        given(customerService.getCustomer(customer.getId())).willReturn(customer);

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));

    }

}
