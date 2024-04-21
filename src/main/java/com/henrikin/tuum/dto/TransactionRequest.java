package com.henrikin.tuum.dto;

import com.henrikin.tuum.model.Currency;
import com.henrikin.tuum.model.Direction;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

/**
 * Data object for a request to create a new transaction.
 */
@Data
@Builder
public class TransactionRequest {
    @NotNull
    @Positive
    private Long accountId;
    @NotNull
    private Double amount;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Direction direction;
    @NotNull
    private String description;

    public TransactionRequest(Long accountId, Double amount, Currency currency, Direction direction, String description) {
        this.accountId = accountId;
        if (amount < 0.01)
            throw new IllegalArgumentException("Amount must be greater than or equal to 0.01.");
        this.amount = Math.round(amount * 100.0) / 100.0;
        this.currency = currency;
        this.direction = direction;
        this.description = description;
    }
}
