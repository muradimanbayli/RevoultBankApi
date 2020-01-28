package com.imanbayli.revolut.repository;

import com.imanbayli.revolut.model.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> getAccountById(String accountId);

    boolean saveAccount(Account account);
}