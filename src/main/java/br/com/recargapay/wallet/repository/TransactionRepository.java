package br.com.recargapay.wallet.repository;

import br.com.recargapay.wallet.entity.Transaction;
import br.com.recargapay.wallet.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKeyAndType(UUID idempotencyKey, TransactionType type);

    @Query("""
            SELECT COALESCE(SUM(
                CASE
                    WHEN t.type IN ('DEPOSITED', 'TRANSFER_IN') THEN amount
                    WHEN t.type IN ('WITHDRAWN', 'TRANSFER_OUT') THEN -amount
                    ELSE 0
                END
            ), 0) AS balance
            FROM Transaction t
            WHERE t.wallet.id = :walletId
            AND createdAt <= :at
            """)
    BigDecimal findByHistoricalBalance(@Param("walletId") UUID walletId, @Param("at") Instant at);

}

