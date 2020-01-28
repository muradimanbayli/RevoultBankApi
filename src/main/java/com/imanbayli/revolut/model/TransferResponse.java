package com.imanbayli.revolut.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransferResponse {

    private String transactionId;
    private String transferDate;
    private String message;

    public static TransferResponse createResponse(){
        TransferResponse response = new TransferResponse();
        response.setMessage("Your transfer completed successfully");
        response.setTransactionId(UUID.randomUUID().toString());
        response.setTransferDate(LocalDateTime.now().toString());
        return response;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
