package com.cliento.clientointerview.cardreader;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This CardReaderSimulator will simulate a 5 second delay when processing a card transaction.
 *
 * The processing happens on a separate thread and the result will be communicated back on the passed in callback
 *
 * Amounts equal or below 0 will fail with a "Negative or zero amount" message
 * Amounts below 500 will succeed with a transaction reference starting with TXREF followed by a number. e.g. TXREF1
 * Amounts over 500 will fail with a "not enough funds" message
 *
 */
@Component
public class CardReaderSimluator implements CardReader {

    private AtomicLong transactionRefs = new AtomicLong(1);
    private ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void startCardTransaction(BigDecimal amount, CardTransactionCallback callback) {

        // Delay execution by 5 seconds, to simulate pin entry and communication with bank
        //
        threadPool.schedule(() -> {
            if(amount.compareTo(BigDecimal.ZERO) <= 0) {
                callback.onFailure("Transaction Denied: Negative or zero amount");
            }else if(amount.compareTo(BigDecimal.valueOf(500)) >= 0) {
                callback.onFailure("Transaction Denied: not enough funds");
            }else{
                callback.onSuccess("TXREF"+transactionRefs.incrementAndGet());
            }
        }, 5, TimeUnit.SECONDS);


    }
}
