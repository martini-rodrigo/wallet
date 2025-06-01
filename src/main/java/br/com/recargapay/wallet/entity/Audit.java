package br.com.recargapay.wallet.entity;

import br.com.recargapay.wallet.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audits")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID walletId;
    private UUID userId;
    private UUID transactionId;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private TransactionType eventType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;
}
