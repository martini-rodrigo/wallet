package br.com.pernambucanas.banking.api.queue;

import br.com.pernambucanas.banking.api.config.GcpTestConfig;
import br.com.pernambucanas.banking.api.dto.CustomerInputDTO;
import br.com.pernambucanas.banking.api.queue.producer.CustomerCreateProducer;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.any;

@SpringBootTest
@Import(GcpTestConfig.class)
class CustomerCreateProducerTest {

    @MockBean
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private CustomerCreateProducer customerCreateProducer;

    @Test
    void testPublish() {
        var inputDTO = CustomerInputDTO.builder()
                .document("31369357488")
                .build();

        Mockito.when(pubSubTemplate.publish(any(), any()))
                .thenReturn(null);
        customerCreateProducer.publish(inputDTO);
    }
}