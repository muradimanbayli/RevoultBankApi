package com.imanbayli.revolut.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class AccountTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_shouldThrowIllegalArgumentExceptionWhenIbanIsNull_fail(){
        new Account(null, new BigDecimal(100));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_shouldThrowIllegalArgumentExceptionWhenAmountIsNull_fail(){
        new Account("iban", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_shouldThrowIllegalArgumentExceptionWhenIbanAndAmountAreNull_fail(){
        new Account(null, null);
    }

    @Test
    public void test_shouldBeCreatedNewAccountWhenIbanAndAmountAreNotNull_success(){
        Account account = new Account("iban", new BigDecimal(0));
        Assert.assertEquals("iban", account.getIban());
        Assert.assertEquals(new BigDecimal(0), account.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_shouldThrowIllegalArgumanExceptionWhenDepositAmountIsNull_fail(){
        Account account = new Account("iban", new BigDecimal(100));
        account.deposit(null);
    }

    @Test
    public void test_WhenDepositAmountIsNotNullThenBeAddedMoneyToMainBalance_success(){
        Account account = new Account("iban", new BigDecimal(100));
        account.deposit(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal(110), account.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_whenWithdrawAmountIsNullThenThrowIllegalArgumanException_fail(){
        Account account = new Account("iban", new BigDecimal(100));
        account.withdraw(null);
    }

    @Test
    public void test_whenWithdrawAmountIsNotNullThenBeSubtractedMoneyFromMainBalance_success(){
        Account account = new Account("iban", new BigDecimal(100));
        account.withdraw(new BigDecimal(10));
        Assert.assertEquals(new BigDecimal(90), account.getBalance());
    }

}
