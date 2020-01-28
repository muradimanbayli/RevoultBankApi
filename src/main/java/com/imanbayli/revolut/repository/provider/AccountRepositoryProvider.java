package com.imanbayli.revolut.repository.provider;

import com.imanbayli.revolut.model.Account;
import com.imanbayli.revolut.repository.AccountRepository;

import java.util.HashMap;
import java.util.Optional;

public class AccountRepositoryProvider implements AccountRepository {

    private static HashMap<String, Account> dataStore= new HashMap<>();

    @Override
    public Optional<Account> getAccountById(String accountId) {
        return Optional.ofNullable(dataStore.get(accountId));
    }

    @Override
    public boolean saveAccount(Account account) {
        dataStore.put(account.getIban(), account);
        return true;
    }

}
