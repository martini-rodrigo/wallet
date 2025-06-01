package br.com.recargapay.wallet.enums;

public enum TransactionType {
    WALLET_CREATED,                // Create Wallet
    BALANCE_RETRIEVED,             // Retrieve Balance
    HISTORICAL_BALANCE_RETRIEVED,  // Retrieve Historical Balance
    DEPOSITED,                     // Deposit Funds
    WITHDRAWN,                     // Withdraw Funds
    TRANSFER_IN,                   // Transfer Funds
    TRANSFER_OUT                   // Transfer Funds
}
