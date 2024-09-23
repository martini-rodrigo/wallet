package br.com.pernambucanas.banking.api.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;

@TestConfiguration	
public class GcpTestConfig {

    @Bean
    public CredentialsProvider credentialsProvider() throws IOException {
        Credentials mockCredentials = mock(Credentials.class);
        CredentialsProvider mockCredentialsProvider = mock(CredentialsProvider.class);
        when(mockCredentialsProvider.getCredentials()).thenReturn(mockCredentials);
        return mockCredentialsProvider;
    }

}
