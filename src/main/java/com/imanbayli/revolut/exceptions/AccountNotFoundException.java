package com.imanbayli.revolut.exceptions;

public class AccountNotFoundException extends MoneyTransferException {

    public AccountNotFoundException(String message) {
        super(message);
    }

}
