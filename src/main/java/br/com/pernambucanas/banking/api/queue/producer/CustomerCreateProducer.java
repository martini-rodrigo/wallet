package br.com.pernambucanas.banking.api.queue.producer;

import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerCreateProducer {

	@Value("${pubsub.create-customer-request.topic.name}")
	private String createCustomerRequestTopic;

	private final PubSubTemplate pubSubTemplate;

	public CustomerInputDTO publish(CustomerInputDTO inputDTO) {
		log.info("publishing customer [{}] in topic [{}] ", inputDTO.getDocument(), createCustomerRequestTopic);
		pubSubTemplate.publish(createCustomerRequestTopic, inputDTO);
		return inputDTO;
	}
}
