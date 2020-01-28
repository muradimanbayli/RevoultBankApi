package com.imanbayli.revolut.model;

import java.math.BigDecimal;

public class Account {

    private String iban;
    private BigDecimal balance;

    public Account(String iban, BigDecimal amount){
        if(iban == null || amount == null){
            throw new IllegalArgumentException("Iban or amount can not be null for creating new account");
        }

        this.iban = iban;
        this.balance = amount;
    }


    public void deposit(BigDecimal depositAmount) {
        if(depositAmount == null) {
            throw new IllegalArgumentException("You can not deposited amount is null");
        }
        this.balance = balance.add(depositAmount);
    }

    public void withdraw(BigDecimal withdrawAmount) {
        if(withdrawAmount == null) {
            throw new IllegalArgumentException("You can not withdraw money amount is null");
        }
        this.balance = balance.subtract(withdrawAmount);
    }

    public String getIban() {
        return iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return iban != null ? iban.equals(account.iban) : account.iban == null;
    }

    @Override
    public int hashCode() {
        return iban != null ? iban.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Account{" +
                "iban='" + iban + '\'' +
                ", balance=" + balance +
                '}';
    }
}
