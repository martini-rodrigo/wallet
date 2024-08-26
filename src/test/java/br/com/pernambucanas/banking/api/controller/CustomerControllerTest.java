package br.com.pernambucanas.banking.api.controller;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAbout_ShouldReturnValidationError() throws Exception {
        var inputDTO = CustomerInputDTO.builder()
                .document("00000000000")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customer")
                        .content(JsonUtils.converObjectToJsonInString(inputDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("Name is required.")))
                .andExpect(jsonPath("$.errors", hasItem("Invalid document.")));
    }
}