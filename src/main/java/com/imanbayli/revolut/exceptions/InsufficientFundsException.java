package com.imanbayli.revolut.exceptions;

public class InsufficientFundsException extends MoneyTransferException {

    public InsufficientFundsException(String message) {
        super(message);
    }

}
