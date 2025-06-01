package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.WithdrawFundsRequestDTO;
import br.com.recargapay.wallet.entity.Transaction;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.exception.DuplicateOperationException;
import br.com.recargapay.wallet.exception.InsufficientFundsException;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.TransactionRepository;
import br.com.recargapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletWithdrawServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuditKafkaProducer auditKafkaProducer;


    @Test
    void shouldWithdrawFundsSuccessfully() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal withdrawAmount = BigDecimal.valueOf(50.00);

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(userId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        WithdrawFundsRequestDTO requestDTO = new WithdrawFundsRequestDTO(userId, withdrawAmount, idempotencyKey);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.WITHDRAWN))
                .thenReturn(Optional.empty());

        walletService.withdrawFunds(requestDTO);

        assertEquals(BigDecimal.valueOf(50.00), wallet.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
        verify(walletRepository).save(wallet);
        verify(auditKafkaProducer).send(any());
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal withdrawAmount = BigDecimal.valueOf(200.00);

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(userId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        WithdrawFundsRequestDTO requestDTO = new WithdrawFundsRequestDTO(userId, withdrawAmount, idempotencyKey);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.WITHDRAWN))
                .thenReturn(Optional.empty());

        assertThrows(InsufficientFundsException.class, () -> walletService.withdrawFunds(requestDTO));

        verify(transactionRepository, never()).save(any());
        verify(auditKafkaProducer, never()).send(any());
    }

    @Test
    void shouldThrowWhenDuplicateWithdraw() {
        UUID userId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(30.00);

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(userId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        WithdrawFundsRequestDTO requestDTO = new WithdrawFundsRequestDTO(userId, amount, idempotencyKey);

        when(walletRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));
        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.WITHDRAWN))
                .thenReturn(Optional.of(new Transaction()));

        assertThrows(DuplicateOperationException.class, () -> walletService.withdrawFunds(requestDTO));
    }
}