package com.cliento.clientointerview.cardreader;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cliento.clientointerview.jdbc.CashRegisterStorage;
import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleResponse;

@Service
public class ClientoSale implements SaleService{
	
	@Autowired
	CashRegisterStorage cashRegisterStorage;

	@Override
	public Optional<SaleResponse> doSale(Sale sale, CardTransaction cardTransaction) {
		
		long customerId = cashRegisterStorage.createCustomer(sale.getCustomer().getName(), sale.getCustomer().getEmail());
		if (customerId < 0) {
			cardTransaction.setFailureCode("Error creating Customer");
			cardTransaction.setSuccess(false);
			return Optional.empty();
		}
		
		long saleId = cashRegisterStorage.createSale(customerId, cardTransaction.getCardTransactionRef(), sale.getItems());
		
		if (saleId < 0) {
			cardTransaction.setFailureCode("Error creating Sale");
			cardTransaction.setSuccess(false);
			return Optional.empty();
		}
		
		return Optional.ofNullable(SaleResponse.create(saleId, cardTransaction.getCardTransactionRef())); 
    }

}
