package com.henrikin.tuum.mapper;

import com.henrikin.tuum.dto.AccountDto;
import com.henrikin.tuum.dto.AccountRequest;
import com.henrikin.tuum.dto.TransactionDto;
import com.henrikin.tuum.model.*;
import com.henrikin.tuum.dto.TransactionResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Mapper class to translate requests to entities and entities to DTO-s, and vice versa.
 */
@Component
public class DataMapper {

    public static Account accountRequestToAccount(AccountRequest accountRequest) {
        Account account = Account.builder()
                .id(null)
                .customerId(accountRequest.getCustomerId())
                .country(accountRequest.getCountry())
                .balances(new ArrayList<>())
                .transactions(new ArrayList<>())
                .build();
        for (Currency currency : accountRequest.getCurrencies()) {
            Balance balance = Balance.builder()
                    .currency(currency)
                    .amount(0.0)
                    .account(account)
                    .build();
            account.getBalances().add(balance);
        }
        return account;
    }

    public static TransactionResponseDto transactionToTransactionResponse(Transaction transaction) {
        Double balanceAfterTransaction = transaction.getAccount().getBalances()
                .stream()
                .filter(balance -> balance.getCurrency() == transaction.getCurrency())
                .findFirst()
                .map(Balance::getAmount)
                .orElse(Double.NaN);

        return TransactionResponseDto.builder()
                .transactionDto(DataMapper.transactionToTransactionDto(transaction))
                .balanceAfterTransaction(balanceAfterTransaction)
                .build();
    }

    public static TransactionDto transactionToTransactionDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .currency(transaction.getCurrency())
                .direction(transaction.getDirection())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .build();
    }

    public static AccountDto accountToAccountDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .customerId(account.getCustomerId())
                .balances(account.getBalances())
                .build();
    }
}
