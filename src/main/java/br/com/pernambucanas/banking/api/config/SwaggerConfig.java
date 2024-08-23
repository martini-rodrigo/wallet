package br.com.pernambucanas.banking.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPICustomer() {
        return new OpenAPI().info(new Info()
                .title("Banking API")
                .version("1.0")
                .description("This API exposes endpoints to manage API  ."));
    }

}
