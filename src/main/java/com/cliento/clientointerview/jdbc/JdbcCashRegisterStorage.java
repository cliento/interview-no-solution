package com.cliento.clientointerview.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleItem;

@Component
public class JdbcCashRegisterStorage implements CashRegisterStorage {
	
	final static Logger logger = Logger.getLogger(JdbcCashRegisterStorage.class);
	
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCashRegisterStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public long createCustomer(String customerName, String customerEmail) {
    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	
    	String query="INSERT INTO customer(name, email) VALUES(?,?);";
    	try {
    		PreparedStatementCreator psc = new PreparedStatementCreator() {
    			@Override
    			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
    				PreparedStatement ps = connection.prepareStatement(query, new String[] {"id"});
    				ps.setString(1, customerName);
    				ps.setString(2, customerEmail);
    				return ps;
    			}
    		};
    		
    		jdbcTemplate.update(psc, keyHolder);
    		
    		return keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	
    	return -1L;
    }
    
    

    @Override
    public long createSale(long customerId, String cardTransactionRef, List<SaleItem> items) {

    	KeyHolder keyHolder = new GeneratedKeyHolder();
    	
    	String saleQuery = "INSERT INTO sale(customer_id, cardTransactionRef, amount) VALUES (?,?,?);";
    	String saleItemQuery = "INSERT INTO sale_item(sale_id, price, description) VALUES (?,?,?);";
    	
    	try {
			
	    	PreparedStatementCreator psc = new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(saleQuery, new String[] {"id"});
					ps.setLong(1, customerId);
					ps.setString(2, cardTransactionRef);
					ps.setBigDecimal(3, getAmount(items));
					return ps;
				}
			};
			
			jdbcTemplate.update(psc, keyHolder);
			
			long saleId = keyHolder.getKey().longValue();
	    	
	    	jdbcTemplate.batchUpdate(saleItemQuery, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					SaleItem item = items.get(i);
					ps.setLong(1, saleId);
					ps.setBigDecimal(2, item.getPrice());
					ps.setString(3, item.getDescription());
				}
				
				@Override
				public int getBatchSize() {
					return items.size();
				}
			});
    	
	    	return saleId;
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    	}
    	return -1L;
    }

    @Override
    public List<Sale> getSales() {
    	List<Sale> sales = new ArrayList<Sale>();
    	try {
			String selectQuery = "SELECT sale.customer_id, sale.cardTransactionRef, sale.amount, customer.name, customer.email, sale_item.price, sale_item.description "
					+ "FROM sale "
					+ "JOIN customer ON sale.customer_id = customer.id "
					+ "LEFT JOIN sale_item ON sale_item.sale_id = sale.id";
			SaleRowCallbackHandler srCallbackHandler = new SaleRowCallbackHandler();
			jdbcTemplate.query(selectQuery, srCallbackHandler);
			sales = srCallbackHandler.getSaleList();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	return sales;
    }
    
    private BigDecimal getAmount(List<SaleItem> items) {
    	return items.stream()
    			.map(SaleItem::getPrice)
    			.reduce(BigDecimal::add)
    			.get();
    }
}
