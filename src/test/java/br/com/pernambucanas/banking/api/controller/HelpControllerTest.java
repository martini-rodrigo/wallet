package br.com.pernambucanas.banking.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(HelpController.class)
public class HelpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAbout_ShouldReturnAboutResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/help/about"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").exists());
    }
}