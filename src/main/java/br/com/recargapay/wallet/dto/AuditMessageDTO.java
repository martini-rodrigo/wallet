package br.com.recargapay.wallet.dto;

import br.com.recargapay.wallet.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditMessageDTO {

    private TransactionType eventType;
    private UUID userId;
    private UUID walletId;
    private UUID transactionId;
    private Instant createdAt;
    private String payload;
}
