package com.henrikin.tuum.controller;

import com.henrikin.tuum.dto.AccountRequest;
import com.henrikin.tuum.dto.TransactionRequest;
import com.henrikin.tuum.messaging.MessagePublisher;
import com.henrikin.tuum.model.Account;
import com.henrikin.tuum.model.Currency;
import com.henrikin.tuum.model.Direction;
import com.henrikin.tuum.service.AccountService;
import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountApiImplTest {

    @Autowired
    private AccountApi api;

    @Test
    void cannotCreateAccountWithNullValues() {
        AccountRequest draftAccount1 = AccountRequest.builder()
                .country("Estonia")
                .currencies(Set.of(Currency.EUR))
                .build();
        AccountRequest draftAccount2 = AccountRequest.builder()
                .customerId(1)
                .country("")
                .currencies(Set.of(Currency.EUR))
                .build();
        AccountRequest draftAccount3 = AccountRequest.builder()
                .customerId(1)
                .currencies(Set.of(Currency.EUR))
                .build();
        AccountRequest draftAccount4 = AccountRequest.builder()
                .customerId(1)
                .country("Estonia")
                .build();

        ConstraintViolationException ex = assertThrows(ConstraintViolationException.class,
                () -> api.createAccount(draftAccount1));
        assertTrue(ex.getMessage().contains("customerId: must not be null"));
       ex = assertThrows(ConstraintViolationException.class,
                () -> api.createAccount(draftAccount2));
        assertTrue(ex.getMessage().contains("country: must not be empty"));
        ex = assertThrows(ConstraintViolationException.class,
                () -> api.createAccount(draftAccount3));
        assertTrue(ex.getMessage().contains("country: must not be null"));
        ex = assertThrows(ConstraintViolationException.class,
                () -> api.createAccount(draftAccount4));
        assertTrue(ex.getMessage().contains("currencies: must not be null"));
    }
    @Test
    void cannotCreateTransactionWithNullValues() {

        TransactionRequest draftTransaction1 = TransactionRequest.builder()
                .amount(10.0)
                .currency(Currency.EUR)
                .direction(Direction.IN)
                .description("desc")
                .build();
        assertThrows(NullPointerException.class,
                () -> {
                    TransactionRequest draftTransaction2 = TransactionRequest.builder()
                            .accountId(1L)
                            .currency(Currency.EUR)
                            .direction(Direction.IN)
                            .description("desc")
                            .build();
                });
        TransactionRequest draftTransaction3 = TransactionRequest.builder()
                .accountId(1L)
                .amount(10.0)
                .direction(Direction.IN)
                .description("desc")
                .build();
        TransactionRequest draftTransaction4 = TransactionRequest.builder()
                .accountId(1L)
                .amount(10.0)
                .currency(Currency.EUR)
                .description("desc")
                .build();
        TransactionRequest draftTransaction5 = TransactionRequest.builder()
                .accountId(1L)
                .amount(10.0)
                .currency(Currency.EUR)
                .build();
        IllegalArgumentException iaex = assertThrows(IllegalArgumentException.class,
                () -> {
                    TransactionRequest draftTransaction6 = TransactionRequest.builder()
                            .accountId(1L)
                            .amount(0.001)
                            .currency(Currency.EUR)
                            .direction(Direction.IN)
                            .description("desc")
                            .build();
                });
        assertTrue(iaex.getMessage().contains("Amount must be greater than or equal to 0.01."));


        ConstraintViolationException cvex = assertThrows(ConstraintViolationException.class,
                () -> api.createTransaction(draftTransaction1));
        assertTrue(cvex.getMessage().contains("accountId: must not be null"));

        cvex = assertThrows(ConstraintViolationException.class,
                () -> api.createTransaction(draftTransaction3));
        assertTrue(cvex.getMessage().contains("currency: must not be null"));
        cvex = assertThrows(ConstraintViolationException.class,
                () -> api.createTransaction(draftTransaction4));
        assertTrue(cvex.getMessage().contains("direction: must not be null"));
        cvex = assertThrows(ConstraintViolationException.class,
                () -> api.createTransaction(draftTransaction5));
        assertTrue(cvex.getMessage().contains("description: must not be null"));
    }
}