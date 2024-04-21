package com.henrikin.tuum.service;

import com.henrikin.tuum.dto.TransactionRequest;
import com.henrikin.tuum.exception.InsufficientFundsException;
import com.henrikin.tuum.model.*;
import com.henrikin.tuum.repository.AccountRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    private AccountService service;
    @BeforeEach
    void setUp() {
        service = new AccountService(accountRepository);
    }

    @Test
    void createAccount() {
        val draftAccount = Account.builder()
                .id(1L)
                .customerId(1)
                .country("Estonia")
                .balances(new ArrayList<>())
                .transactions(new ArrayList<>())
                .build();
        service.createAccount(draftAccount);
        verify(accountRepository).save(draftAccount);
    }

    @Test
    void createTransaction() {
        val draftAccount = Account.builder()
                .id(1L)
                .customerId(1)
                .country("Estonia")
                .balances(new ArrayList<>())
                .transactions(new ArrayList<>())
                .build();
        val draftBalance = Balance.builder()
                .currency(Currency.EUR)
                .account(draftAccount)
                .amount(10.0)
                .build();
        draftAccount.getBalances().add(draftBalance);

        when(accountRepository.getReferenceById(anyLong())).thenReturn(draftAccount);
        when(accountRepository.save(any())).thenReturn(draftAccount);

        val draftTransactionRequest = TransactionRequest.builder()
                .accountId(1L)
                .currency(Currency.EUR)
                .direction(Direction.IN)
                .amount(10.0)
                .description("desc")
                .build();
        service.createTransaction(draftTransactionRequest);

        assertEquals(draftAccount.getBalances().get(0).getAmount(), 20);
        val draftTransactionRequest2 = TransactionRequest.builder()
                .accountId(1L)
                .currency(Currency.EUR)
                .direction(Direction.OUT)
                .amount(10.0)
                .description("desc")
                .build();
        service.createTransaction(draftTransactionRequest2);
        assertEquals(draftAccount.getBalances().get(0).getAmount(), 10);
    }

    @Test
    void throwsExceptionForInsufficientFunds() {
        val draftAccount = Account.builder()
                .id(1L)
                .customerId(1)
                .country("Estonia")
                .balances(new ArrayList<>())
                .transactions(new ArrayList<>())
                .build();
        val draftBalance = Balance.builder()
                .currency(Currency.EUR)
                .account(draftAccount)
                .amount(10.0)
                .build();
        draftAccount.getBalances().add(draftBalance);

        when(accountRepository.getReferenceById(anyLong())).thenReturn(draftAccount);

        val draftTransactionRequest = TransactionRequest.builder()
                .accountId(1L)
                .currency(Currency.EUR)
                .direction(Direction.OUT)
                .amount(20.0)
                .description("desc")
                .build();
        assertThrows(InsufficientFundsException.class,
                () -> service.createTransaction(draftTransactionRequest)
        );
    }

    @Test
    void throwsExceptionForUnavailableCurrency() {
        val draftAccount = Account.builder()
                .id(1L)
                .customerId(1)
                .country("Estonia")
                .balances(new ArrayList<>())
                .transactions(new ArrayList<>())
                .build();
        val draftBalance = Balance.builder()
                .currency(Currency.EUR)
                .account(draftAccount)
                .amount(10.0)
                .build();
        draftAccount.getBalances().add(draftBalance);

        when(accountRepository.getReferenceById(anyLong())).thenReturn(draftAccount);

        val draftTransactionRequest = TransactionRequest.builder()
                .accountId(1L)
                .currency(Currency.SEK)
                .direction(Direction.OUT)
                .amount(20.0)
                .description("desc")
                .build();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createTransaction(draftTransactionRequest)
        );
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains("Currency: " + Currency.SEK + " is not available for account with id: " + draftAccount.getId()));
    }
}