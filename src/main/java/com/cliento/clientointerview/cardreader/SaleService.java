package com.cliento.clientointerview.cardreader;

import java.util.Optional;

import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleResponse;

public interface SaleService {
	
	/**
     * Creates a customer and a sale item in database
     *
     * @param sale - the Sale item to be converted and saved to database
     * @param cardTransaction - A CardTransaction object for setting messages and getting the cardTransactionReference
     *
     * @return an Optional with the SaleResponse on success or an empty Optional on exception
     */
	Optional<SaleResponse> doSale(Sale sale, CardTransaction cardTransaction);
	
}
