package br.com.pernambucanas.banking.api.controller.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AboutResponse {

    private String version;

    public AboutResponse() {
        this.version = getAppVersion();
    }

    public String getVersion() {
        return version;
    }

    private String getAppVersion() {
        String version = "UNKNOWN";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                "META-INF/maven/br.com.pernambucanas.banking/api/pom.properties")) {
            if (inputStream != null) {
                Properties properties = new Properties();
                properties.load(inputStream);
                version = properties.getProperty("version", "UNKNOWN");
            }
        } catch (IOException e) {
            // handle exceptions here
        }

        return version;
    }
}