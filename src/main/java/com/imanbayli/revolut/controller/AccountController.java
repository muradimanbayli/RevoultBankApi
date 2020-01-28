package com.imanbayli.revolut.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanbayli.revolut.model.Account;
import com.imanbayli.revolut.model.AccountRequest;
import com.imanbayli.revolut.repository.AccountRepository;
import com.imanbayli.revolut.repository.provider.AccountRepositoryProvider;
import com.imanbayli.revolut.service.AccountService;
import com.imanbayli.revolut.service.provider.AccountServiceProvider;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;

public class AccountController {
    private static AccountRepository accountRepository = new AccountRepositoryProvider();
    private static AccountService service = new AccountServiceProvider(accountRepository);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static class CreateAccount implements Route{

        @Override
        public Object handle(Request request, Response response) throws Exception {
            AccountRequest account = mapper.readValue(request.body(), AccountRequest.class);
            service.saveAccount(account.getAccountId(), new BigDecimal(account.getBalance()));
            return mapper.writeValueAsString(account);
        }
    }

    public static class GetAccount implements Route {

        @Override
        public Object handle(Request request, Response response) throws Exception {
            String accountId = request.params(":accountId");
            Account account = service.getAccountById(accountId);
            return mapper.writeValueAsString(account);
        }
    }
}
