package com.cliento.clientointerview.jdbc;

import com.cliento.clientointerview.restapi.Sale;
import com.cliento.clientointerview.restapi.SaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcCashRegisterStorage implements CashRegisterStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCashRegisterStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public long createCustomer(String customerName, String customerEmail) {
        throw new UnsupportedOperationException("method not implemented");
    }

    @Override
    public long createSale(long customerId, String cardTransactionRef, List<SaleItem> items) {

        throw new UnsupportedOperationException("method not implemented");
    }

    @Override
    public List<Sale> getSales() {
        throw new UnsupportedOperationException("method not implemented");
    }
}
