package br.com.pernambucanas.banking.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;

import br.com.pernambucanas.banking.api.utils.JsonUtils;

@Configuration
public class PubSubConfig {

	@Bean
	public JacksonPubSubMessageConverter jacksonPubSubMessageConverter() {
		var mapper = JsonUtils.createObjectMapper();
		return new JacksonPubSubMessageConverter(mapper);
	}
}
