package com.henrikin.tuum.exception;

/**
 * Exception for when the account has insufficient funds.
 */
public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String message) {
        super(message);
    }
}
