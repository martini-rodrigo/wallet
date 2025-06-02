CREATE UNIQUE INDEX uniq_transaction_type_idempotency ON transactions (type, idempotency_key);
CREATE UNIQUE INDEX uniq_wallet_user ON wallets (user_id);
CREATE UNIQUE INDEX uniq_user_document ON users (document);