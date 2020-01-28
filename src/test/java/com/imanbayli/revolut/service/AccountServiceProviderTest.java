package com.imanbayli.revolut.service;

import com.imanbayli.revolut.exceptions.InsufficientFundsException;
import com.imanbayli.revolut.exceptions.MoneyTransferException;
import com.imanbayli.revolut.model.Account;
import com.imanbayli.revolut.repository.AccountRepository;
import com.imanbayli.revolut.service.provider.AccountServiceProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountServiceProviderTest {

    private AccountService accountService;
    private String toAccountId;
    private String fromAccountId;
    private BigDecimal transferAmount;
    private Account fromAccount;
    private Account toAccount;

    private AccountRepository repositoryMock;

    public AccountServiceProviderTest(){
        repositoryMock = Mockito.mock(AccountRepository.class);

        accountService = new AccountServiceProvider(repositoryMock);
        toAccountId = "iban1";
        fromAccountId = "iban2";
        transferAmount = BigDecimal.ONE;

        fromAccount = new Account(fromAccountId, new BigDecimal(100_000));
        toAccount = new Account(toAccountId, new BigDecimal(100_000));


        Mockito.when(repositoryMock.getAccountById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        Mockito.when(repositoryMock.getAccountById(toAccountId)).thenReturn(Optional.of(toAccount));

    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenFromAccountIdIsNullThenShouldBeThrownIllegalArgumentException_fail() {
        accountService.transfer(null, toAccountId, transferAmount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenToAccountIdIsNullThenShouldBeThrownIllegalArgumentException_fail() {
        accountService.transfer(fromAccountId, null, transferAmount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenTransferAmountIsNullThenShouldBeThrownIllegalArgumentException_fail() {
        accountService.transfer(fromAccountId, toAccountId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenAllParametersAreNullThenShouldBeThrownIllegalArgumentException_fail() {
        accountService.transfer(null, null, null);
    }

    @Test(expected = MoneyTransferException.class)
    public void test_whenTransferAmountIsNegativeThenShouldBeThrownMoneyTransferException_fail() {
        accountService.transfer(fromAccountId, toAccountId, new BigDecimal(-1));
    }

    @Test(expected = InsufficientFundsException.class)
    public void test_whenTransferAmountIsGreaterThanAccountBalanceThenShouldBeThrownInsufficientFundsException_fail() {
        accountService.transfer(fromAccountId, toAccountId, new BigDecimal(1000_000));
    }

    @Test
    public void test_whenAllValidationsPassedThenAmountShouldBeTransferred_fail() {
        accountService.transfer(fromAccountId, toAccountId, transferAmount);
        Assert.assertEquals(new BigDecimal(99_999), fromAccount.getBalance());
        Assert.assertEquals(new BigDecimal(100_001), toAccount.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenAccountIdIsNullThenShouldBeThrownIllegalArgumentException_fail(){
        accountService.saveAccount(null, transferAmount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenTransferAmountIdIsNullThenShouldBeThrownIllegalArgumentException_fail(){
        accountService.saveAccount(fromAccountId, null);
    }

    @Test
    public void test_whenAllParametersValidThenAccountShouldBeSaved_success(){
        Mockito.when(repositoryMock.getAccountById("abc")).thenReturn(Optional.empty());
        accountService.saveAccount("abc", transferAmount);
        Mockito.verify(repositoryMock,Mockito.times(1)).saveAccount(Mockito.any());
    }

    @Test
    public void test_whenAccountIdExistThenAccountShouldBeReturned_success(){
        accountService.getAccountById(fromAccountId);
        Mockito.verify(repositoryMock, Mockito.times(1)).getAccountById(fromAccountId);
    }


}
