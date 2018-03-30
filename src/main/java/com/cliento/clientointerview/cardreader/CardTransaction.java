package com.cliento.clientointerview.cardreader;

import java.util.List;

import com.cliento.clientointerview.jdbc.CashRegisterStorage;
import com.cliento.clientointerview.restapi.Sale;

public class CardTransaction implements CardTransactionCallback{
	
	String failureCode;
	String successCode;
	boolean completed = false;
	boolean success = false;
	List<Sale> sales;
	Sale sale;
	CashRegisterStorage cashRegisterStorage;
	//	CardTransactionCallback ctc;
	
//	public CardTransaction(CashRegisterStorage cashRegisterStorage) {
//		this.cashRegisterStorage = cashRegisterStorage;
//	}
	
//	public void run() {
//		this.ctc.onSuccess("transactionRef");
//		this.ctc.onFailure("failureCode");
//	}
	
	public CardTransaction(Sale sale, CashRegisterStorage cashRegisterStorage) {
        this.sale = sale;
        this.cashRegisterStorage = cashRegisterStorage;
    }

	@Override
	public void onFailure(String failureCode) {
		System.out.println(failureCode);
		this.failureCode = failureCode;
		this.completed = true;
		
	}

	@Override
	public void onSuccess(String transactionRef) {
		this.success = true;
		System.out.println(transactionRef);
		this.successCode = transactionRef;
		this.completed = true;
		doSale();
	}
	
    private void doSale() {
		long customerId = cashRegisterStorage.createCustomer(sale.getCustomer().getName(), sale.getCustomer().getEmail());
		cashRegisterStorage.createSale(customerId, this.successCode, sale.getItems());
    }

	public boolean isCompleted() {
		return this.completed;
	}
	
	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sale) {
		this.sales = sale;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
