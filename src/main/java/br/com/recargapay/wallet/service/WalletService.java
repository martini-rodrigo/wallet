package br.com.recargapay.wallet.service;

import br.com.recargapay.wallet.dto.*;
import br.com.recargapay.wallet.entity.Transaction;
import br.com.recargapay.wallet.entity.User;
import br.com.recargapay.wallet.entity.Wallet;
import br.com.recargapay.wallet.enums.TransactionType;
import br.com.recargapay.wallet.exception.BusinessException;
import br.com.recargapay.wallet.exception.DuplicateOperationException;
import br.com.recargapay.wallet.exception.InsufficientFundsException;
import br.com.recargapay.wallet.exception.NotFoundException;
import br.com.recargapay.wallet.kafka.AuditKafkaProducer;
import br.com.recargapay.wallet.repository.TransactionRepository;
import br.com.recargapay.wallet.repository.WalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final UserService userService;

    private final AuditKafkaProducer auditKafkaProducer;


    public Wallet findWalletByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Wallet not found for user {0}", userId));
    }

    public BalanceResponseDTO getCurrentBalance(UUID userId) {
        log.info("Fetching current balance for userId=[{}]", userId);

        Wallet wallet = findWalletByUserId(userId);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                .eventType(TransactionType.BALANCE_RETRIEVED)
                .userId(wallet.getUser().getId())
                .walletId(wallet.getId())
                .createdAt(Instant.now())
                .build();
        auditKafkaProducer.send(auditMessageDTO);

        log.info("Current balance [{}] retrieved successfully for walletId=[{}]", wallet.getBalance(), wallet.getId());
        return new BalanceResponseDTO(wallet.getBalance());
    }

    @Transactional
    public Wallet createWallet(UUID userId) {
        log.info("Start createWallet for userId=[{}]", userId);

        User user = userService.findById(userId);
        log.debug("User found: id=[{}], fullName=[{}]", user.getId(), user.getFullName());

        if (walletRepository.existsByUserId(user.getId())) {
            log.error("User [{}] already has a wallet", user.getId());
            throw new BusinessException("User {0} already has a wallet.", user.getId());
        }

        try {
            Wallet wallet = Wallet.builder()
                    .user(user)
                    .balance(BigDecimal.ZERO)
                    .build();
            Wallet savedWallet = walletRepository.save(wallet);
            log.info("Wallet created successfully. walletId=[{}] for userId=[{}]", savedWallet.getId(), userId);

            AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                    .eventType(TransactionType.WALLET_CREATED)
                    .userId(user.getId())
                    .walletId(savedWallet.getId())
                    .createdAt(Instant.now())
                    .build();
            auditKafkaProducer.send(auditMessageDTO);

            return savedWallet;
        } catch (Exception e) {
            // can occur when two requests create a wallet simultaneously.
            log.error("Concurrency error creating wallet for userId=[{}].", userId, e);
            throw new BusinessException("User {0} already has a wallet.", user.getId());
        }
    }

    @Transactional
    public void depositFunds(DepositFundsRequestDTO dto) {
        log.info("Start depositFunds: UserId=[{}] amount=[{}]", dto.getUserId(), dto.getAmount());

        Wallet wallet = findWalletByUserId(dto.getUserId());
        TransactionContext context = new TransactionContext(wallet, dto.getAmount(), dto.getIdempotencyKey(),
                TransactionType.DEPOSITED, dto.getUserId());
        processTransaction(context, dto);
    }

    @Transactional
    public void withdrawFunds(WithdrawFundsRequestDTO dto) {
        log.info("Start withdrawFunds: UserId=[{}] amount=[{}]", dto.getUserId(), dto.getAmount());

        Wallet wallet = findWalletByUserId(dto.getUserId());
        TransactionContext context = new TransactionContext(wallet, dto.getAmount(), dto.getIdempotencyKey(),
                TransactionType.WITHDRAWN, dto.getUserId());
        processTransaction(context, dto);
    }

    @Transactional
    public void transferFunds(TransferFundsRequestDTO dto) {
        log.info("Start transferFunds: fromUserId=[{}] toUserId=[{}] amount=[{}]", dto.getFromUserId(), dto.getToUserId(), dto.getAmount());

        Wallet fromWallet = findWalletByUserId(dto.getFromUserId());
        Wallet toWallet = findWalletByUserId(dto.getToUserId());

        TransactionContext contextOut = new TransactionContext(fromWallet, dto.getAmount(), dto.getIdempotencyKey(),
                TransactionType.TRANSFER_OUT, dto.getFromUserId());
        processTransaction(contextOut, dto);

        TransactionContext contextIn = new TransactionContext(toWallet, dto.getAmount(), dto.getIdempotencyKey(),
                TransactionType.TRANSFER_IN, dto.getToUserId());
        processTransaction(contextIn, dto);
    }

    public void processTransaction(TransactionContext context, Object requestPayload) {
        log.info("Start {} for userId=[{}] with amount=[{}]", context.type(), context.userId(), context.amount());

        checkDuplicateOperation(context);
        updateWalletBalance(context);

        try {
            Transaction transaction = Transaction.builder()
                    .type(context.type())
                    .amount(context.amount())
                    .wallet(context.wallet())
                    .idempotencyKey(context.idempotencyKey())
                    .build();
            transactionRepository.save(transaction);
            log.info("{} transaction saved. transactionId=[{}]", context.type(), transaction.getId());

            sendAuditMessage(context, transaction, requestPayload);
        } catch (Exception e) {
            log.error("Failed to process {} for userId=[{}]: {}", context.type(), context.userId(), e.getMessage(), e);
            throw new BusinessException("Failed to process " + context.type().name().toLowerCase(), e);
        }
    }

    private void checkDuplicateOperation(TransactionContext context) {
        Optional<Transaction> existing = transactionRepository.findByIdempotencyKeyAndType(
                context.idempotencyKey(), context.type());

        if (existing.isPresent()) {
            log.warn("Duplicate operation detected. idempotencyKey=[{}], type=[{}]", context.idempotencyKey(), context.type());
            throw new DuplicateOperationException("Operation already completed.");
        }
    }

    private void updateWalletBalance(TransactionContext context) {
        Wallet wallet = context.wallet();
        BigDecimal amount = context.amount();

        switch (context.type()) {
            case DEPOSITED, TRANSFER_IN:{
                wallet.setBalance(wallet.getBalance().add(amount));
                break;
            }
            case WITHDRAWN, TRANSFER_OUT:{
                if (wallet.getBalance().compareTo(amount) < 0) {
                    log.warn("Insufficient funds. walletId=[{}] balance=[{}], requestedAmount=[{}]",
                            wallet.getId(), wallet.getBalance(), amount);
                    throw new InsufficientFundsException("Insufficient funds.");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
                break;
            }
            default:
                throw new BusinessException("Unsupported transaction type [{}]", context.type());
        }

        walletRepository.save(wallet);
        log.info("Wallet updated. walletId=[{}] newBalance=[{}]", wallet.getId(), wallet.getBalance());
    }

    private void sendAuditMessage(TransactionContext context, Transaction transaction, Object requestPayload) throws JsonProcessingException {
        AuditMessageDTO audit = AuditMessageDTO.builder()
                .eventType(context.type())
                .userId(context.wallet().getUser().getId())
                .walletId(context.userId())
                .transactionId(transaction.getId())
                .createdAt(Instant.now())
                .payload(new ObjectMapper().writeValueAsString(requestPayload))
                .build();

        auditKafkaProducer.send(audit);
    }
}
