package com.henrikin.tuum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents the balance of an account in a currency.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"id","account"})
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private Double amount;

    @ManyToOne
    private Account account;

    public void deposit(Double amount) {
        this.amount += amount;
    }

    public void withdraw(Double amount) {
        this.amount -= amount;
    }
}
