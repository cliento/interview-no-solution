package com.cliento.clientointerview.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowCallbackHandler;

import com.cliento.clientointerview.restapi.Customer;
import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleItem;

public class SaleRowCallbackHandler implements RowCallbackHandler {
	List<Sale> sales = new ArrayList<Sale>();
	Map<Long, Customer> customerMap = new HashMap<Long, Customer>();
	Map<Long, List<SaleItem>> saleItemMap = new HashMap<Long, List<SaleItem>>();
	Map<Long, String> cardTransMap = new HashMap<Long, String>();
	
	public void processRow(ResultSet rs) throws SQLException {
		
		long customerId = (long)rs.getLong("customer_id");
		Customer customer = Customer.create(rs.getString("name"), rs.getString("email"));
		if(!customerMap.containsKey(customerId)) {
			customerMap.put(customerId, customer);
		}
		
		SaleItem saleItem = SaleItem.create(rs.getString("description"), rs.getBigDecimal("price"));
		if(saleItemMap.containsKey(customerId)) {
			saleItemMap.get(customerId).add(saleItem);
		} else {
			List<SaleItem> saleList = new ArrayList<SaleItem>();
			saleList.add(saleItem);
			saleItemMap.put(customerId, saleList);
		}
		
		if(!cardTransMap.containsKey(customerId)) {
			cardTransMap.put(customerId, rs.getString("cardTransactionRef"));
		}
			
	}
	
	public List<Sale> getSaleList() {
		for (Long customerKey : customerMap.keySet()) {
			sales.add(Sale.create(cardTransMap.get(customerKey), customerMap.get(customerKey), saleItemMap.get(customerKey)));
		}
		return sales;
	}
}
