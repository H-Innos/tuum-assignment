package com.henrikin.tuum.controller;

import com.henrikin.tuum.dto.AccountDto;
import com.henrikin.tuum.dto.TransactionDto;
import com.henrikin.tuum.dto.AccountRequest;
import com.henrikin.tuum.dto.TransactionRequest;
import com.henrikin.tuum.dto.TransactionResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Interface for the application API.
 */
@Validated
public interface AccountApi {

    /**
     * Creates an account and returns its information.
     * @param accountRequest    account creation request
     * @return                  data object containing account id, customer id and a list of balances
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/accounts",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    ResponseEntity<AccountDto> createAccount(@RequestBody @Valid AccountRequest accountRequest);

    /**
     * Returns account with the specified id.
     * @param accountId account id
     * @return          the corresponding account
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/accounts/{accountId}",
            produces = { "application/json" }
    )
    ResponseEntity<AccountDto> getAccount(@PathVariable("accountId") Long accountId);

    /**
     * Returns list of transactions for the account.
     * @param accountId account id
     * @return          list of transactions
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/transactions/{accountId}",
            produces = { "application/json" }
    )
    ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable("accountId") Long accountId);

    /**
     * Creates a new transaction and processes it.
     * @param transactionRequest    request to create a transaction
     * @return                      data object containing information about a successful transaction
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/transactions",
            consumes = { "application/json" },
            produces = { "application/json" }
    )
    ResponseEntity<TransactionResponseDto> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest);

    /*
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/accounts",
            produces = { "application/json" }
    )
    ResponseEntity<List<AccountDto>> getAccounts();
    */
}
