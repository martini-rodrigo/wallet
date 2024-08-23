package br.com.pernambucanas.banking.api.queue.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerCreateProducer {

	@Value("${pubsub.create-customer-request.topic.name}")
	private String createCustomerRequestTopic;
	@Autowired
	private PubSubTemplate pubSubTemplate;

	public void publish(CustomerInputDTO inputDTO) {
		log.info("publishing customer [{}] in topic [{}] ", inputDTO.getDocument(), createCustomerRequestTopic);
		pubSubTemplate.publish(createCustomerRequestTopic, inputDTO);
	}
}
