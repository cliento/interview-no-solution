package com.cliento.clientointerview.cardreader;

import java.math.BigDecimal;

public interface CardReader {

    /**
     * Initiates a card transaction for the given amount, the transaction is asynchronous and the result will be
     * available on completion through the given callback
     *
     * @param amount sale amount as a BigDecimal
     * @param callback a callback to handle the result on completion
     */
    void startCardTransaction(BigDecimal amount, CardTransactionCallback callback);
}
