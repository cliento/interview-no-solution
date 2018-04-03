package com.cliento.clientointerview.cardreader;

import java.util.concurrent.CountDownLatch;

public class CardTransaction implements CardTransactionCallback{
	
	CountDownLatch cardTransactionLatch;
	String cardTransactionRef;
	String failureCode;
	boolean success = false;
	
	public CardTransaction(CountDownLatch cardTransactionLatch) {
        this.cardTransactionLatch = cardTransactionLatch;
    }

	@Override
	public void onFailure(String failureCode) {
		this.failureCode = failureCode;
		cardTransactionLatch.countDown();
	}

	@Override
	public void onSuccess(String transactionRef) {
		this.success = true;
		this.cardTransactionRef = transactionRef;
		cardTransactionLatch.countDown();
	}
	
	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCardTransactionRef() {
		return cardTransactionRef;
	}

	public void setCardTransactionRef(String cardTransactionRef) {
		this.cardTransactionRef = cardTransactionRef;
	}
	
}
