package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.AuditMessageDTO;
import br.com.recargapay.wallet.dto.BalanceResponseDTO;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.exception.BusinessException;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.TransactionRepository;
import br.com.recargapay.wallet.util.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletService walletService;

    private final TransactionRepository transactionRepository;

    private final AuditKafkaProducer auditKafkaProducer;


    public BalanceResponseDTO getHistoricalBalance(UUID userId, LocalDateTime at) {
        log.info("Fetching historical balance for userId=[{}] at [{}]", userId, at);

        Wallet wallet = walletService.findWalletByUserId(userId);
        try {
            Instant instant = DateUtils.convertToInstant(at);
            BigDecimal balance = transactionRepository.findByHistoricalBalance(wallet.getId(), instant);

            AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                    .eventType(TransactionType.HISTORICAL_BALANCE_RETRIEVED)
                    .userId(wallet.getUser().getId())
                    .walletId(wallet.getId())
                    .createdAt(Instant.now())
                    .payload(new ObjectMapper().writeValueAsString(Map.of("queryInstant", at.toString())))
                    .build();
            auditKafkaProducer.send(auditMessageDTO);

            log.info("Historical balance [{}] retrieved successfully for walletId=[{}]", balance, wallet.getId());
            return new BalanceResponseDTO(balance);
        } catch (Exception e) {
            log.error("Error fetching historical balance for userId=[{}] at [{}]", userId, at, e);
            throw new BusinessException("Failed to retrieve historical balance", e);
        }
    }
}
