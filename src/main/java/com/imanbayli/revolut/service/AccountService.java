package com.imanbayli.revolut.service;

import com.imanbayli.revolut.model.Account;

import java.math.BigDecimal;

public interface AccountService {
    void transfer(String fromAccountId, String toAccountId, BigDecimal transferAmount);
    Account getAccountById(String accountId);
    void saveAccount(String accountId, BigDecimal amount);
}
