package br.com.pernambucanas.banking.api.service;


import br.com.pernambucanas.banking.api.config.GcpTestConfig;
import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.when;

@SpringBootTest
@Import(GcpTestConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTest {

    @MockBean
    private CustomerCreateProducer customerCreateProducer;
    
    @Autowired
    private CustomerService customerService;


    @Test
    @Order(1)
    void testCreateNewCustomer() {
        var inputDTO = CustomerInputDTO.builder()
                .companyId(23L)
                .name("Name")
                .document("3333333369")
                .contact(CustomerInputDTO.CostumerContactInputDTO.builder()
                        .phone("333333333")
                        .build())
                .build();

        when(customerCreateProducer.publish(inputDTO))
                .thenReturn(inputDTO);
        customerService.save(inputDTO);
    }

}
