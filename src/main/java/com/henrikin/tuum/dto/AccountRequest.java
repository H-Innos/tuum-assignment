package com.henrikin.tuum.dto;

import com.henrikin.tuum.model.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * Data object for a request to create an account.
 */
@Data
@AllArgsConstructor
@Builder
public class AccountRequest {
    @NotNull
    private final Integer customerId;
    @NotNull
    @NotEmpty
    private final String country;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Set<Currency> currencies;
}