package com.henrikin.tuum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transaction DTO including the balance after transaction.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private TransactionDto transactionDto;
    private double balanceAfterTransaction;
}
