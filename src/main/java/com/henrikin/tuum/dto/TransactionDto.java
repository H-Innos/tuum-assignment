package com.henrikin.tuum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.henrikin.tuum.model.Currency;
import com.henrikin.tuum.model.Direction;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for the Transaction class.
 */
@Data
@Builder
@JsonRootName("transaction")
public class TransactionDto {
    @JsonProperty("id")
    private final Long id;
    @JsonProperty("account_id")
    private final Long accountId;
    @JsonProperty("currency")
    private final Currency currency;
    @JsonProperty("direction")
    private final Direction direction;
    @JsonProperty("amount")
    private final Double amount;
    @JsonProperty("description")
    private final String description;
}
