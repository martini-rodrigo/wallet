package br.com.recargapay.wallet.service;


import br.com.recargapay.wallet.dto.AuditMessageDTO;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuditKafkaProducer auditKafkaProducer;

    @InjectMocks
    private WalletService walletService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }

    @Test
    void shouldCreateWalletWhenUserHasNoWallet() {
        when(userService.findById(userId)).thenReturn(user);
        when(walletRepository.existsByUserId(userId)).thenReturn(false);

        Wallet savedWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);

        Wallet result = walletService.createWallet(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertNotNull(result.getId());

        ArgumentCaptor<AuditMessageDTO> auditCaptor = ArgumentCaptor.forClass(AuditMessageDTO.class);
        verify(auditKafkaProducer, times(1)).send(auditCaptor.capture());
        verify(userService, times(1)).findById(userId);
        verify(walletRepository, times(1)).existsByUserId(userId);
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }
}
