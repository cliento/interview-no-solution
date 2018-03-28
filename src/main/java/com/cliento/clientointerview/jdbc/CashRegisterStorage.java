package com.cliento.clientointerview.jdbc;

import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleItem;

import java.util.List;

public interface CashRegisterStorage {

    /**
     * Creates a customer
     *
     * @param customerName - the customer name
     * @param customerEmail - the customer email
     *
     * @return a long with the allocated customer id
     */
    long createCustomer(String customerName, String customerEmail);

    /**
     * Create a sale with the given sale items linked to the given customer id and card transaction ref
     *
     * @param customerId the customer id
     * @param cardTransactionRef the card tx ref
     * @param items a list of sale items
     *
     * @return a long with the allocated saleid
     */
    long createSale(long customerId, String cardTransactionRef, List<SaleItem> items);


    /**
     * Returns a list of all the sales in storage
     *
     * @return a list of Sale items
     */
    List<Sale> getSales();

}
