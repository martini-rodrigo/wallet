package br.com.pernambucanas.banking.api.controller;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.enums.AccountType;
import br.com.pernambucanas.banking.api.enums.GenderType;
import br.com.pernambucanas.banking.api.enums.MaritalStatusType;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import br.com.pernambucanas.banking.api.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @MockBean
    private CustomerCreateProducer customerCreateProducer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSuccess() throws Exception {
        var inputDTO = CustomerInputDTO.builder()
                .name("Test")
                .document("75111303057")
                .gender(GenderType.M.name())
                .maritalStatus(MaritalStatusType.MARRIED.name())
                .email("email@gmai.com")
                .rg("3125587777")
                .birthDate(LocalDate.now())
                .managerId(1)
                .address(CustomerInputDTO.CostumerAddressInputDTO.builder()
                        .address("Rua Joao")
                        .number("123")
                        .state("SP")
                        .city("Sao")
                        .neighborhood("Vila")
                        .postalCode("0999922")
                        .build())
                .account(CustomerInputDTO.CostumerAccountInputDTO.builder()
                        .type(AccountType.CC.name())
                        .build())
                .build();
        when(customerCreateProducer.publish(inputDTO))
                .thenReturn(inputDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .header("companyId", 1)
                        .content(JsonUtils.converObjectToJsonInString(inputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void testInvalidDocument() throws Exception {
        var inputDTO = CustomerInputDTO.builder()
                .document("00000000000")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .content(JsonUtils.converObjectToJsonInString(inputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Invalid document.")));
    }

    @Test
    void testInvalidEmail() throws Exception {
        var inputDTO = CustomerInputDTO.builder()
                .document("75111303057")
                .email("email.com")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .content(JsonUtils.converObjectToJsonInString(inputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Invalid email.")));
    }

    @Test
    void testAddressIsRequired() throws Exception {
        var inputDTO = CustomerInputDTO.builder()
                .name("Test")
                .document("75111303057")
                .gender(GenderType.F.name())
                .maritalStatus(MaritalStatusType.SINGLE.name())
                .email("email@gmai.com")
                .rg("3125587777")
                .birthDate(LocalDate.now())
                .managerId(1)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .content(JsonUtils.converObjectToJsonInString(inputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Address is required.")));
    }
}