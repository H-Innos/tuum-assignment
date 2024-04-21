package com.henrikin.tuum.service;

import com.henrikin.tuum.dto.TransactionRequest;
import com.henrikin.tuum.exception.InsufficientFundsException;
import com.henrikin.tuum.model.*;
import com.henrikin.tuum.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service class for all actions.
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account getAccount(Long accountId) {
        return accountRepository.getReferenceById(accountId);
    }

    @Transactional
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }


    @Transactional
    public List<Transaction> getTransactions(Long accountId) {
        val account = accountRepository.getReferenceById(accountId);
        return account.getTransactions();
    }

    /**
     * Persists the transaction into the database and updates user balance.
     * If data is invalid, throws an exception.
     */
    @Transactional
    public Transaction createTransaction(TransactionRequest transactionRequest) {
        Long accountId = transactionRequest.getAccountId();
        val account = accountRepository.getReferenceById(accountId);
        Balance balance = getBalance(transactionRequest, account);

        val transaction = Transaction.builder()
                .currency(balance.getCurrency())
                .amount(transactionRequest.getAmount())
                .direction(transactionRequest.getDirection())
                .description(transactionRequest.getDescription())
                .build();
        transaction.setAccount(account);
        account.getTransactions().add(transaction);
        accountRepository.save(account);

        processTransaction(transaction, balance);

        return transaction;
    }

    private static void processTransaction(Transaction transaction, Balance balance) {
        if (transaction.getDirection() == Direction.IN) {
            balance.deposit(transaction.getAmount());
        }
        if (transaction.getDirection() == Direction.OUT)
            balance.withdraw(transaction.getAmount());
    }

    private static Balance getBalance(TransactionRequest transactionRequest, Account account) {
        Balance balance = null;
        for (Balance b : account.getBalances()) {
            if (b.getCurrency() == transactionRequest.getCurrency())
                balance = b;
        }
        if (balance == null)
            throw new IllegalArgumentException("Currency: " + transactionRequest.getCurrency() + " is not available for account with id: " + account.getId());
        if (transactionRequest.getDirection() == Direction.OUT && balance.getAmount().compareTo(transactionRequest.getAmount()) < 0)
            throw new InsufficientFundsException("Account has insufficient funds");
        return balance;
    }

    /*
    @Transactional
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
    */
}