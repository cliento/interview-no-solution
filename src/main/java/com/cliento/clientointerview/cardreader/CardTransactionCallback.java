package com.cliento.clientointerview.cardreader;

public interface CardTransactionCallback {

    void onFailure(String failureCode);
    void onSuccess(String transactionRef);
}
