package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.BalanceResponseDTO;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.TransactionRepository;
import br.com.recargapay.wallet.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private WalletService walletService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuditKafkaProducer auditKafkaProducer;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGetHistoricalBalanceSuccess() {
        UUID userId = UUID.randomUUID();
        LocalDateTime at = LocalDateTime.now().minusDays(1);
        Instant instant = DateUtils.convertToInstant(at);

        User user = User.builder().id(userId).build();
        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .build();

        BigDecimal balance = BigDecimal.valueOf(1000);

        when(walletService.findWalletByUserId(userId)).thenReturn(wallet);
        when(transactionRepository.findByHistoricalBalance(wallet.getId(), instant)).thenReturn(balance);

        BalanceResponseDTO result = transactionService.getHistoricalBalance(userId, at);

        assertNotNull(result);
        assertEquals(balance, result.getBalance());

        verify(auditKafkaProducer).send(argThat(audit ->
                audit.getEventType() == TransactionType.HISTORICAL_BALANCE_RETRIEVED &&
                        audit.getUserId().equals(userId) &&
                        audit.getWalletId().equals(wallet.getId()) &&
                        audit.getPayload().contains(at.toString())
        ));
    }


}


