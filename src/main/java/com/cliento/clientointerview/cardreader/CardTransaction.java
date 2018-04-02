package com.cliento.clientointerview.cardreader;

import java.util.concurrent.CountDownLatch;

import com.cliento.clientointerview.jdbc.CashRegisterStorage;
import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleResponse;

public class CardTransaction implements CardTransactionCallback{
	
	CashRegisterStorage cashRegisterStorage;
	CountDownLatch cardTransactionLatch;
	Sale sale;
	SaleResponse saleResponse;
	String failureCode;
	String cardTransactionRef;
	boolean success = false;
	
	public CardTransaction(Sale sale, CashRegisterStorage cashRegisterStorage, CountDownLatch cardTransactionLatch) {
        this.sale = sale;
        this.cashRegisterStorage = cashRegisterStorage;
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
		doSale();
		cardTransactionLatch.countDown();
	}
	
    private void doSale() {
		Long customerId = cashRegisterStorage.createCustomer(sale.getCustomer().getName(), sale.getCustomer().getEmail());
		if (customerId < 0) {
			this.failureCode = "Error creating Customer";
			this.success = false;
			return;
		}
		long saleId = cashRegisterStorage.createSale(customerId, this.cardTransactionRef, sale.getItems());
		if (saleId < 0) {
			this.failureCode = "Error creating Sale";
			this.success = false;
			return;
		}
		this.saleResponse = SaleResponse.create(saleId, this.cardTransactionRef); 
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

	public SaleResponse getSaleResponse() {
		return saleResponse;
	}

	public void setSaleResponse(SaleResponse saleResponse) {
		this.saleResponse = saleResponse;
	}
	
}
