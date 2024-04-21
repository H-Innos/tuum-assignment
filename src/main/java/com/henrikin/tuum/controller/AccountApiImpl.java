package com.henrikin.tuum.controller;

import com.henrikin.tuum.dto.AccountDto;
import com.henrikin.tuum.dto.TransactionDto;
import com.henrikin.tuum.mapper.DataMapper;
import com.henrikin.tuum.messaging.MessagePublisher;
import com.henrikin.tuum.dto.AccountRequest;
import com.henrikin.tuum.model.Transaction;
import com.henrikin.tuum.dto.TransactionRequest;
import com.henrikin.tuum.dto.TransactionResponseDto;
import com.henrikin.tuum.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@Valid
@RequiredArgsConstructor
public class AccountApiImpl implements AccountApi{

    private final AccountService accountService;
    private final MessagePublisher messagePublisher;

    @Override
    public ResponseEntity<AccountDto> createAccount(AccountRequest accountRequest) {
        val createdAccount = accountService.createAccount(DataMapper.accountRequestToAccount(accountRequest));
        messagePublisher.sendMessage("Successfully created account with id: " + createdAccount.getId());
        log.info("Created account: ");
        return ResponseEntity.status(CREATED)
                .body(DataMapper.accountToAccountDto(createdAccount));
    }

    @Override
    public ResponseEntity<AccountDto> getAccount(Long accountId) {
        val account = accountService.getAccount(accountId);
        return ResponseEntity.ok()
                .body(DataMapper.accountToAccountDto(account));
    }

    @Override
    public ResponseEntity<List<TransactionDto>> getTransactions(Long accountId) {
        List<Transaction> transactions = accountService.getTransactions(accountId);
        return ResponseEntity.ok()
                .body(transactions.stream()
                        .map(DataMapper::transactionToTransactionDto)
                        .toList());
    }

    @Override
    public ResponseEntity<TransactionResponseDto> createTransaction(TransactionRequest transactionRequest) {
        val createdTransaction = accountService.createTransaction(transactionRequest);
        messagePublisher.sendMessage("Transaction created successfully.");
        return ResponseEntity.ok()
                .body(DataMapper.transactionToTransactionResponse(createdTransaction));
    }

    /*
    @Override
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        return ResponseEntity.ok()
                .body(accounts.stream()
                        .map(ApplicationMapper::accountToAccountDto)
                        .toList());
    }
     */
}
