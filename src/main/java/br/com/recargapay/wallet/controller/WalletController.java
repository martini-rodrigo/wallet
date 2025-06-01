package br.com.recargapay.wallet.controller;

import br.com.recargapay.wallet.dto.BalanceResponseDTO;
import br.com.recargapay.wallet.dto.DepositFundsRequestDTO;
import br.com.recargapay.wallet.dto.TransferFundsRequestDTO;
import br.com.recargapay.wallet.dto.WithdrawFundsRequestDTO;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.service.TransactionService;
import br.com.recargapay.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Wallet", description = "Operations related to users' digital wallets")
public class WalletController {

    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping("/{userId}")
    @Operation(summary = "Create wallet", description = "Creates a new digital wallet for the given user")
    public ResponseEntity<UUID> createWallet(@PathVariable UUID userId) {
        Wallet wallet = walletService.createWallet(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wallet.getId());
    }

    @GetMapping("/{userId}/balance")
    @Operation(summary = "Get current balance", description = "Retrieves the current balance of the user's wallet")
    public ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.getCurrentBalance(userId));
    }

    @GetMapping("/{userId}/historical-balance")
    public ResponseEntity<BalanceResponseDTO> getHistoricalBalance(
            @PathVariable UUID userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @Parameter(
                    schema = @Schema(type = "string", format = "date-time", example = "2025-06-01 00:00:00")
            )
            LocalDateTime at) {
        BalanceResponseDTO balanceResponseDTO = transactionService.getHistoricalBalance(userId, at);
        return ResponseEntity.ok(balanceResponseDTO);
    }

    @PostMapping("/{userId}/deposit")
    @Operation(summary = "Deposit funds", description = "Deposits funds into the user's wallet")
    public ResponseEntity<Void> depositFunds(@PathVariable UUID userId,
                                             @Valid @RequestBody DepositFundsRequestDTO depositFundsRequestDTO,
                                             @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        depositFundsRequestDTO.setUserId(userId);
        depositFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.depositFunds(depositFundsRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{userId}/withdraw")
    @Operation(summary = "Withdraw funds", description = "Withdraws funds from the user's wallet")
    public ResponseEntity<Void> withdrawFunds(@PathVariable UUID userId,
                                              @Valid @RequestBody WithdrawFundsRequestDTO withdrawFundsRequestDTO,
                                              @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        withdrawFundsRequestDTO.setUserId(userId);
        withdrawFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.withdrawFunds(withdrawFundsRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds", description = "Transfers funds between wallets")
    public ResponseEntity<Void> transferFunds(@Valid @RequestBody TransferFundsRequestDTO transferFundsRequestDTO,
                                              @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        transferFundsRequestDTO.setIdempotencyKey(idempotencyKey);
        walletService.transferFunds(transferFundsRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}