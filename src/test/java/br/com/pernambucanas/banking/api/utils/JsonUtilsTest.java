package br.com.pernambucanas.banking.api.utils;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilsTest {

    @Test
    void testConvertObjectToJsonInString() {
        var inputDTO = CustomerInputDTO.builder()
                .companyId(23L)
                .name("Name")
                .document("3333333369")
                .contact(CustomerInputDTO.CostumerContactInputDTO.builder()
                        .phone("333333333")
                        .build())
                .address(CustomerInputDTO.CostumerAddressInputDTO.builder()
                        .address("Rua Joao")
                        .build())
                .build();

        var jsonInString = JsonUtils.converObjectToJsonInString(inputDTO);
        assertTrue(jsonInString.contains("3333333369"));
        assertTrue(jsonInString.contains("Name"));
    }

    @Test
    void testConvertJsonInStringToObject() {
        var inputDTO = CustomerInputDTO.builder()
                .companyId(23L)
                .name("Name")
                .document("3333333369")
                .contact(CustomerInputDTO.CostumerContactInputDTO.builder()
                        .phone("333333333")
                        .build())
                .address(CustomerInputDTO.CostumerAddressInputDTO.builder()
                        .address("Rua Joao")
                        .build())
                .build();

        var jsonInString = JsonUtils.converObjectToJsonInString(inputDTO);
        var jsonObject = (CustomerInputDTO) JsonUtils.convertJsonInStringToObject(jsonInString, CustomerInputDTO.class);

        assertTrue(Objects.nonNull(jsonObject));
        assertEquals(jsonObject.getName(), inputDTO.getName());
        assertEquals(jsonObject.getDocument(), inputDTO.getDocument());
        assertEquals(jsonObject.getContact().getPhone(), inputDTO.getContact().getPhone());
        assertEquals(jsonObject.getAddress().getAddress(), inputDTO.getAddress().getAddress());
    }

    @Test
    void testCreateObjectMapper() {
        var objectMapper = JsonUtils.createObjectMapper();
        assertTrue(Objects.nonNull(objectMapper));
    }

    @Test
    void testErrorWhileConvertJsonInStringToObject() {
        BusinessException exception = assertThrows(BusinessException.class, () ->
                JsonUtils.convertJsonInStringToObject("test", ArrayList.class));
        assertTrue(exception.getMessage().contains("Error convert json to object."));
    }

    @Test
    void testCreatePrettyPrintJson() {
        var inputDTO = CustomerInputDTO.builder()
                .companyId(23L)
                .name("Name")
                .document("3333333369")
                .contact(CustomerInputDTO.CostumerContactInputDTO.builder()
                        .phone("333333333")
                        .build())
                .address(CustomerInputDTO.CostumerAddressInputDTO.builder()
                        .address("Rua Joao")
                        .build())
                .build();
        var jsonObject = JsonUtils.createPrettyPrintJson(inputDTO);
        assertNotNull(jsonObject);
    }

}
