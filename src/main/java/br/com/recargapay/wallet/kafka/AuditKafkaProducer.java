package br.com.recargapay.wallet.kafka;

import br.com.recargapay.wallet.dto.AuditMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditKafkaProducer {

    private final KafkaTemplate<String, AuditMessageDTO> kafkaTemplate;

    public AuditKafkaProducer(KafkaTemplate<String, AuditMessageDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(AuditMessageDTO dto) {
        log.debug("Sending audit message to Kafka: [{}]", dto);
        kafkaTemplate.sendDefault(dto);
        log.info("Audit message sent to Kafka for walletId=[{}]", dto.getWalletId());
    }
}
