package br.com.pernambucanas.banking.api.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DocumentUtilsTest {

    @Test
    void testValidDocument() {
        var isValidCPF = DocumentUtils.isValidCPF("75111303057");
        assertTrue(isValidCPF);
    }

    @Test
    void testInvalidDocument() {
        var isValidCPF = DocumentUtils.isValidCPF("00000000022");
        assertTrue(!isValidCPF);
    }

}
