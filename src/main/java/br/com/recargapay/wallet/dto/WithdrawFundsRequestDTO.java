package br.com.recargapay.wallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class WithdrawFundsRequestDTO {

    @JsonIgnore
    private UUID userId;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Withdraw amount must be greater than zero")
    private BigDecimal amount;

    @JsonIgnore
    private UUID idempotencyKey;

}
