package com.imanbayli.revolut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanbayli.revolut.model.TransferRequest;
import com.imanbayli.revolut.model.TransferResponse;
import com.imanbayli.revolut.repository.AccountRepository;
import com.imanbayli.revolut.repository.provider.AccountRepositoryProvider;
import com.imanbayli.revolut.service.AccountService;
import com.imanbayli.revolut.service.provider.AccountServiceProvider;
import spark.Request;
import spark.Response;
import spark.Route;

import java.math.BigDecimal;

public class TransferController {
    private static AccountRepository accountRepository = new AccountRepositoryProvider();
    private static AccountService service = new AccountServiceProvider(accountRepository);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public static class Transfer implements Route {

        @Override
        public Object handle(Request request, Response response) throws Exception {
            response.header("Content-Type", "application/json");

            TransferRequest transferRequest = mapper.readValue(request.body(), TransferRequest.class);

            BigDecimal transferAmount = transferRequest.getAmount() == null ? null : new BigDecimal(transferRequest.getAmount());
            service.transfer(transferRequest.getSourceAccountId(), transferRequest.getTargetAccountId(), transferAmount);

            response.status(200);
            return mapper.writeValueAsString(TransferResponse.createResponse());
        }

    }
}
