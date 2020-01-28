package com.imanbayli.revolut.service;

import com.imanbayli.revolut.model.Account;
import com.imanbayli.revolut.repository.AccountRepository;
import com.imanbayli.revolut.repository.provider.AccountRepositoryProvider;
import com.imanbayli.revolut.service.provider.AccountServiceProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.*;

public class AccountServiceProviderConcurrencyTest {

    private AccountService service;
    private AccountRepository repositoryMock;

    private Account accountOne;
    private Account accountTwo;

    private BigDecimal transferAmount = BigDecimal.ONE;
    private BigDecimal sumOfAccountsBalanceBeforeOperations;

    public AccountServiceProviderConcurrencyTest() {
        repositoryMock = Mockito.mock(AccountRepository.class);
        service = new AccountServiceProvider(repositoryMock);

        accountOne = new Account("iban1", new BigDecimal(100_000));
        accountTwo = new Account("iban2", new BigDecimal(100_000));

        Mockito.when(repositoryMock.getAccountById(accountOne.getIban())).thenReturn(Optional.of(accountOne));
        Mockito.when(repositoryMock.getAccountById(accountTwo.getIban())).thenReturn(Optional.of(accountTwo));

        sumOfAccountsBalanceBeforeOperations = accountOne.getBalance().add(accountTwo.getBalance());
    }

    @Test
    public void test_whenMultipleOperationsHappenAtTheSameTimeThenDeadlockShouldNotBeHappen_success() throws Exception {
        Thread[] operations = new Thread[100];

        for (int i = 0; i < operations.length; i++) {
            operations[i] = new Thread(() -> doTenThousandTransfers());
            operations[i].start();
        }

        Thread.sleep(5 * 1000);

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long ids[] = bean.findMonitorDeadlockedThreads();

        if (ids != null) {
            Assert.fail("Your method is not protected from deadlock");
        }
    }


    @Test
    public void test_whenMultipleTransfersHappenAtTheSameTimeThenSumOfAccountBalancesAlwaysBeSame_success() throws InterruptedException {
        Thread[] operations = new Thread[100];

        for (int i = 0; i < operations.length; i++) {
            operations[i] = new Thread(() -> doTenThousandTransfers());
            operations[i].start();
        }

        for (int i = 0; i < operations.length; i++) {
            operations[i].join();
        }

        BigDecimal sumOfBalancesAfterOperations = accountOne.getBalance().add(accountTwo.getBalance());
        Assert.assertEquals(sumOfBalancesAfterOperations, sumOfAccountsBalanceBeforeOperations);
    }

    private void doTenThousandTransfers() {
        Random random = new Random();
        for (int j = 0; j < 10_000; j++) {
            int randomIndex = random.nextInt(2);

            String from = randomIndex == 0 ? accountOne.getIban() : accountTwo.getIban();
            String to = randomIndex == 0 ? accountTwo.getIban() : accountOne.getIban();

            service.transfer(from, to, transferAmount);

        }
    }

}
