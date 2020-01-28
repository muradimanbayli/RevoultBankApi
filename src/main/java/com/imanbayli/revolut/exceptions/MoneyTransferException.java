package com.imanbayli.revolut.exceptions;

public class MoneyTransferException extends RuntimeException  {

    public MoneyTransferException(String message) {
        super(message);
    }

}
