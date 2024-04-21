package com.henrikin.tuum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.henrikin.tuum.model.Balance;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO for account class.
 */
@Data
@Builder
public class AccountDto {
    @JsonProperty("id")
    private final Long id;
    @JsonProperty("customer_id")
    private final Integer customerId;
    @JsonProperty("balances")
    private final List<Balance> balances;
}
