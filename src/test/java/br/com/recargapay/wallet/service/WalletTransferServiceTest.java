package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.TransferFundsRequestDTO;
import br.com.recargapay.wallet.entity.Transaction;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.exception.DuplicateOperationException;
import br.com.recargapay.wallet.exception.InsufficientFundsException;
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
class WalletTransferServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuditKafkaProducer auditKafkaProducer;

    @Test
    void shouldTransferFundsSuccessfully() {
        UUID fromUserId = UUID.randomUUID();
        UUID toUserId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(40.00);

        Wallet fromWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(fromUserId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        Wallet toWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(toUserId).build())
                .balance(BigDecimal.valueOf(50.00))
                .build();

        TransferFundsRequestDTO dto = new TransferFundsRequestDTO(fromUserId, toUserId, amount, idempotencyKey);

        when(walletRepository.findByUserId(fromUserId)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByUserId(toUserId)).thenReturn(Optional.of(toWallet));

        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.TRANSFER_OUT))
                .thenReturn(Optional.empty());

        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.TRANSFER_IN))
                .thenReturn(Optional.empty());

        walletService.transferFunds(dto);

        assertEquals(BigDecimal.valueOf(90.00), toWallet.getBalance());
        assertEquals(BigDecimal.valueOf(60.00), fromWallet.getBalance());

        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(auditKafkaProducer, times(2)).send(any());
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {
        UUID fromUserId = UUID.randomUUID();
        UUID toUserId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(150.00);

        Wallet fromWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(fromUserId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        Wallet toWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(toUserId).build())
                .balance(BigDecimal.valueOf(30.00))
                .build();

        TransferFundsRequestDTO dto = new TransferFundsRequestDTO(fromUserId, toUserId, amount, idempotencyKey);

        when(walletRepository.findByUserId(fromUserId)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByUserId(toUserId)).thenReturn(Optional.of(toWallet));

        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.TRANSFER_OUT))
                .thenReturn(Optional.empty());

        assertThrows(InsufficientFundsException.class, () -> walletService.transferFunds(dto));

        verify(transactionRepository, never()).save(any());
        verify(walletRepository, never()).save(toWallet);
        verify(auditKafkaProducer, never()).send(any());
    }

    @Test
    void shouldThrowWhenDuplicateTransferOut() {
        UUID fromUserId = UUID.randomUUID();
        UUID toUserId = UUID.randomUUID();
        UUID idempotencyKey = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(40.00);

        Wallet fromWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(fromUserId).build())
                .balance(BigDecimal.valueOf(100.00))
                .build();

        Wallet toWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(User.builder().id(toUserId).build())
                .balance(BigDecimal.valueOf(50.00))
                .build();

        TransferFundsRequestDTO dto = new TransferFundsRequestDTO(fromUserId, toUserId, amount, idempotencyKey);

        when(walletRepository.findByUserId(fromUserId)).thenReturn(Optional.of(fromWallet));
        when(walletRepository.findByUserId(toUserId)).thenReturn(Optional.of(toWallet));

        when(transactionRepository.findByIdempotencyKeyAndType(idempotencyKey, TransactionType.TRANSFER_OUT))
                .thenReturn(Optional.of(new Transaction()));

        assertThrows(DuplicateOperationException.class, () -> walletService.transferFunds(dto));
    }
}