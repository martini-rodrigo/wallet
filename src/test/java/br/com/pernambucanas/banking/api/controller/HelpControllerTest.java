package br.com.pernambucanas.banking.api.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;


@WebMvcTest(HelpController.class)
public class HelpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String POM_PROPERTIES_PATH = "META-INF/maven/br.com.pernambucanas.banking/api/pom.properties";
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @BeforeAll
    public static void init() throws Exception {
        File dir = new File(TEMP_DIR, "META-INF/maven/br.com.pernambucanas.banking/api");
        File tempFile = new File(dir, "pom.properties");

        try (Writer writer = new FileWriter(tempFile)) {
            Properties properties = new Properties();
            properties.setProperty("version", "1.0.0");
            properties.store(writer, null);
        }

        File classpathDir = new File("target/test-classes");
        File targetFile = new File(classpathDir, POM_PROPERTIES_PATH);
        targetFile.getParentFile().mkdirs();
        Files.copy(tempFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void getAbout_ShouldReturnAboutResponse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/help/about"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").exists());
    }
}