package com.imanbayli.revolut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanbayli.revolut.controller.AccountController;
import com.imanbayli.revolut.controller.TransferController;
import com.imanbayli.revolut.model.ErrorResponse;
import spark.Spark;

public class Application {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer(){
        Spark.port(8080);

        Spark.post("/v1/transfer", new TransferController.Transfer());
        Spark.post("/v1/account", new AccountController.CreateAccount());
        Spark.get("/v1/account/:accountId", new AccountController.GetAccount());

        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body(convertExceptionToJson(exception));
        });
    }

    public static String convertExceptionToJson(Exception e) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setCode("E0000000");
            errorResponse.setMessage(e.getMessage());
            return mapper.writeValueAsString(errorResponse);
        } catch (Exception ex) {
            return "";
        }
    }

}
