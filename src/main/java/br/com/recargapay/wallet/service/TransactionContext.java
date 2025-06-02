package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;


public record TransactionContext(Wallet wallet, BigDecimal amount, UUID idempotencyKey, TransactionType type,
                                 UUID userId) {
}