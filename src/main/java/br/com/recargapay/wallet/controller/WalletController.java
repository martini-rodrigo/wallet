package br.com.recargapay.wallet.controller;

import br.com.recargapay.wallet.dto.BalanceResponseDTO;
import br.com.recargapay.wallet.dto.DepositFundsRequestDTO;
import br.com.recargapay.wallet.dto.TransferFundsRequestDTO;
import br.com.recargapay.wallet.dto.WithdrawFundsRequestDTO;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.service.TransactionService;
import br.com.recargapay.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    private final TransactionService transactionService;

    @PostMapping("/{userId}")
    public ResponseEntity<UUID> createWallet(@PathVariable UUID userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet.getId());
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable UUID userId) {
        BalanceResponseDTO balanceResponseDTO = walletService.getCurrentBalance(userId);
        return ResponseEntity.ok(balanceResponseDTO);
    }

    @GetMapping("/{userId}/historical-balance")
    public ResponseEntity<BalanceResponseDTO> getHistoricalBalance(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime at) {
        BalanceResponseDTO balanceResponseDTO = transactionService.getHistoricalBalance(userId, at);
        return ResponseEntity.ok(balanceResponseDTO);
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Void> depositFunds(@PathVariable UUID userId,
                                             @Valid @RequestBody DepositFundsRequestDTO depositFundsRequestDTO,
                                             @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        depositFundsRequestDTO.setUserId(userId);
        depositFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.depositFunds(depositFundsRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Void> withdrawFunds(@PathVariable UUID userId,
                                             @Valid @RequestBody WithdrawFundsRequestDTO withdrawFundsRequestDTO,
                                             @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        withdrawFundsRequestDTO.setUserId(userId);
        withdrawFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.withdrawFunds(withdrawFundsRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferFunds(@Valid @RequestBody TransferFundsRequestDTO transferFundsRequestDTO,
                                              @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        transferFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.transferFunds(transferFundsRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
