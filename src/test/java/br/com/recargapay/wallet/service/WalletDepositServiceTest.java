package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.DepositFundsRequestDTO;
import br.com.recargapay.wallet.entity.Transaction;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.exception.DuplicateOperationException;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.TransactionRepository;
import br.com.recargapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletDepositServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuditKafkaProducer auditKafkaProducer;


    @Test
    void shouldDepositFundsSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal depositAmount = BigDecimal.valueOf(100.30);

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(userId).build())
                .balance(BigDecimal.ZERO)
                .build();

        DepositFundsRequestDTO requestDTO = new DepositFundsRequestDTO(userId, depositAmount, idempotencyKey);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.DEPOSITED))
                .thenReturn(Optional.empty());

        walletService.depositFunds(requestDTO);

        assertEquals(BigDecimal.valueOf(100.30), wallet.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
        verify(walletRepository).save(wallet);
        verify(auditKafkaProducer).send(any());
    }

    @Test
    void shouldThrowWhenDuplicateDeposit() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(50.00);

        DepositFundsRequestDTO request = new DepositFundsRequestDTO(userId, amount, idempotencyKey);
        Wallet wallet = Wallet.builder()
                .user(User.builder().id(userId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.DEPOSITED))
                .thenReturn(Optional.of(new Transaction()));

        assertThrows(DuplicateOperationException.class, () -> walletService.depositFunds(request));
    }
}