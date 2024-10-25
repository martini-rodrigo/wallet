package br.com.pernambucanas.banking.api.config;

import br.com.pernambucanas.banking.api.handler.FeignExceptionHandler;
import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients("br.com.pernambucanas.banking.api.client")
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignExceptionHandler();
    }

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(2L), 1);
    }
}
