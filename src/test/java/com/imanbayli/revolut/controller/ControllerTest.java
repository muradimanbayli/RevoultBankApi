package com.imanbayli.revolut.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanbayli.revolut.Application;
import com.imanbayli.revolut.model.AccountRequest;
import com.imanbayli.revolut.model.TransferRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeClass
    public static void setup() {
        Application.startServer();
    }


    @Test
    public void test_whenRequestBodyIsEmptyStatusShouldBeBadRequest_fail() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new AccountRequest())))
                .uri(URI.create("http://localhost:8080/account"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void test_whenAllParametersAreValidThenNewAccountShouldBeCreated_success() throws IOException, InterruptedException {
        AccountRequest account = new AccountRequest();
        account.setAccountId("acc0001");
        account.setBalance("1000");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create("http://localhost:8080/account"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    public void test_whenAccountIdIsNotExistThenStatusShouldBeBadRequest_fail() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/account/14578994"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void test_whenAccountIdIsExistThenAccountShouldBeReturn_success() throws IOException, InterruptedException {
        AccountRequest account = new AccountRequest();
        account.setAccountId("acc0002");
        account.setBalance("1000");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create("http://localhost:8080/account"))
                .header("Content-Type", "application/json")
                .build();

        HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/account/"+account.getAccountId()))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    public void test_whenRequestBodyIsEmptyThenTransferStatusShouldBeBadRequest_fail() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(new TransferRequest())))
                .uri(URI.create("http://localhost:8080/transfer"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void test_whenAccountIdIsNotExistThenTransferStatusShouldBeBadRequest_fail() throws IOException, InterruptedException {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId("someId");
        transferRequest.setTargetAccountId("someId2");
        transferRequest.setAmount("1000");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(transferRequest)))
                .uri(URI.create("http://localhost:8080/transfer"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(400, response.statusCode());
    }

    @Test
    public void test_whenAllparametersAreValidThenAmountShouldBeTransfered_fail() throws IOException, InterruptedException {
        AccountRequest account = new AccountRequest();
        account.setAccountId("AC202000001");
        account.setBalance("1000");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create("http://localhost:8080/account"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        AccountRequest account2 = new AccountRequest();
        account2.setAccountId("AC202000002");
        account2.setBalance("1000");

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account2)))
                .uri(URI.create("http://localhost:8080/account"))
                .header("Content-Type", "application/json")
                .build();
        response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSourceAccountId(account.getAccountId());
        transferRequest.setTargetAccountId(account2.getAccountId());
        transferRequest.setAmount("10");

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(transferRequest)))
                .uri(URI.create("http://localhost:8080/transfer"))
                .header("Content-Type", "application/json")
                .build();

        response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertEquals(200, response.statusCode());
    }
}
