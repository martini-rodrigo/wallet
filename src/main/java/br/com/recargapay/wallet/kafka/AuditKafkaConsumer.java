package br.com.recargapay.wallet.kafka;

import br.com.recargapay.wallet.dto.AuditMessageDTO;
import br.com.recargapay.wallet.entity.Audit;
import br.com.recargapay.wallet.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditKafkaConsumer {

    private final AuditRepository auditRepository;

    @Transactional
    @KafkaListener(topics = "audit_log", groupId = "audit-consumer-group")
    public void listen(AuditMessageDTO auditMessageDTO) {
        log.info("Consumer Audit for walletId=[{}] eventType=[{}]", auditMessageDTO.getWalletId(), auditMessageDTO.getEventType());
        try {
            Audit audit = Audit.builder()
                    .createdAt(auditMessageDTO.getCreatedAt())
                    .eventType(auditMessageDTO.getEventType())
                    .walletId(auditMessageDTO.getWalletId())
                    .userId(auditMessageDTO.getUserId())
                    .transactionId(auditMessageDTO.getTransactionId())
                    .payload(auditMessageDTO.getPayload())
                    .build();

            auditRepository.save(audit);
            log.info("Audit saved for walletId=[{}] eventType=[{}]", audit.getWalletId(), audit.getEventType());
        } catch (Exception e) {
            log.error("Error saving audit", e);
        }
    }
}
