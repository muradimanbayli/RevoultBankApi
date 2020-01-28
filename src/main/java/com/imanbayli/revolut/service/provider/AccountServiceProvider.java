package com.imanbayli.revolut.service.provider;

import com.imanbayli.revolut.exceptions.AccountAlreadyExistException;
import com.imanbayli.revolut.exceptions.AccountNotFoundException;
import com.imanbayli.revolut.exceptions.InsufficientFundsException;
import com.imanbayli.revolut.exceptions.MoneyTransferException;
import com.imanbayli.revolut.model.Account;
import com.imanbayli.revolut.repository.AccountRepository;
import com.imanbayli.revolut.service.AccountService;

import java.math.BigDecimal;

public class AccountServiceProvider implements AccountService {

    private AccountRepository repository;

    public AccountServiceProvider(AccountRepository accountRepository) {
        this.repository = accountRepository;
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, BigDecimal transferAmount) {
        validate(fromAccountId, toAccountId, transferAmount);

        Account fromAccount = repository.getAccountById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account toAccount = repository.getAccountById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));

        validateSourceAccountBalance(fromAccount, transferAmount);

        coreTransfer(fromAccount, toAccount, transferAmount);
    }

    @Override
    public Account getAccountById(String accountId) {
        return repository.getAccountById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public void saveAccount(String accountId, BigDecimal amount) {
        if(accountId == null){
            throw new IllegalArgumentException("Account Id could not be null");
        }

        if(amount == null){
            throw new IllegalArgumentException("Amount could not be null");
        }

        if(repository.getAccountById(accountId).isPresent()){
            throw new AccountAlreadyExistException("This account already exist");
        }
        repository.saveAccount(new Account(accountId, amount));
    }

    private void validate(String fromAccountId, String toAccountId, BigDecimal transferAmount) {
        if (fromAccountId == null || toAccountId == null || transferAmount == null) {
            throw new IllegalArgumentException("from, to or amount is null");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new MoneyTransferException("The transfer could not proceed between the same accounts");
        }

        if (BigDecimal.ZERO.compareTo(transferAmount) > 0) {
            throw new MoneyTransferException("The transfer could not proceed with negative amount");
        }
    }


    private final Object lock = new Object();

    private void coreTransfer(Account fromAccount, Account toAccount, BigDecimal transferAmount) {
        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);


        if (fromHash < toHash) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    fromAccount.withdraw(transferAmount);
                    toAccount.deposit(transferAmount);
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    fromAccount.withdraw(transferAmount);
                    toAccount.deposit(transferAmount);
                }
            }
        } else {
            synchronized (lock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        fromAccount.withdraw(transferAmount);
                        toAccount.deposit(transferAmount);
                    }
                }
            }
        }
    }

    private void validateSourceAccountBalance(Account fromAccount, BigDecimal transferAmount) {
        if (transferAmount.compareTo(fromAccount.getBalance()) > 0) {
            throw new InsufficientFundsException("Your account does not have enough money");
        }
    }


}
