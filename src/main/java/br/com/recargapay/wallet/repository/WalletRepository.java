package br.com.recargapay.wallet.repository;

import br.com.recargapay.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    boolean existsByUserId(UUID userId);

    Optional<Wallet> findByUserId(UUID userId);
}
