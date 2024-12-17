package dsw.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsw.springframework.spring6restmvc.controllers.CustomerController;
import dsw.springframework.spring6restmvc.model.CustomerDTO;
import dsw.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
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
    ArgumentCaptor<CustomerDTO> customerCaptor;

    private CustomerDTO customer1;
    private CustomerDTO customer2;
    private CustomerDTO customer3;
    
    @BeforeEach
    void setUp() {
        customer1 = CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 1").build();
        customer2 = CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 2").build();
        customer3 = CustomerDTO.builder().id(UUID.randomUUID()).customerName("Customer 3").build();
        given(customerService.getAllCustomers()).willReturn(List.of(customer1, customer2, customer3));
    }
    
    @Test
    void testCreateNewCustomer() throws Exception {
        UUID id = UUID.randomUUID();
        CustomerDTO customer = CustomerDTO.builder()
                .customerName("Test Customer")
                .id(id)
                .build();

        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customer);

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

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer1.getId())
                        .content(objectMapper.writeValueAsString(customer1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).updateCustomer(idCaptor.capture(), any(CustomerDTO.class));

        assertThat(customer1.getId()).isEqualTo(idCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomer(idCaptor.capture(), customerCaptor.capture());

        assertThat(customer1.getId()).isEqualTo(idCaptor.getValue());
        assertThat(customer1.getCustomerName()).isEqualTo(customerCaptor.getValue().getCustomerName());
    }
    
    @Test
    void testDeleteCustomer() throws Exception {

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(idCaptor.capture());
        assertThat(customer1.getId()).isEqualTo(idCaptor.getValue());
    }
    
    @Test
    void testGetAllCustomers() throws Exception {

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void TestGetCustomerById() throws Exception {
        
        given(customerService.getCustomerById(customer1.getId())).willReturn(Optional.of(customer1));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customer1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer1.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer1.getCustomerName())));

    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
