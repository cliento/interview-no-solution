package com.cliento.clientointerview.restapi;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cliento.clientointerview.cardreader.CardReader;
import com.cliento.clientointerview.cardreader.CardTransaction;
import com.cliento.clientointerview.jdbc.CashRegisterStorage;

@RestController
@EnableAutoConfiguration
public class Api {

	final static Logger logger = Logger.getLogger(Api.class);
	
    private final CardReader cardReader;
    private final CashRegisterStorage cashRegisterStorage;

    @Autowired
    public Api(CardReader cardReader, CashRegisterStorage cashRegisterStorage) {
        this.cardReader = cardReader;
        this.cashRegisterStorage = cashRegisterStorage;
    }

    @GetMapping(path="/hello-world")
    public ResponseEntity<?> helloWorld(){
        return ResponseEntity.ok("Hello World!");
    }
    
    @GetMapping(path="/sales")
    public ResponseEntity<?> getSales(){
    	List<Sale> sales = cashRegisterStorage.getSales();
    	return sales != null ? ResponseEntity.ok(Sales.create(sales)) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sales found");
    }
    
    
    @PostMapping(path="/sale")
    public ResponseEntity<?> sale(@RequestBody Sale sale) {
    	final CountDownLatch cardTransactionLatch = new CountDownLatch(1);
    	
    	CardTransaction cardTransaction = new CardTransaction(sale, cashRegisterStorage, cardTransactionLatch);
		cardReader.startCardTransaction(sale.getTotalAmount(), cardTransaction);
		
		try {
			cardTransactionLatch.await();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		
    	return cardTransaction.isSuccess() ? ResponseEntity.ok(cardTransaction.getSaleResponse()) :
    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"" + cardTransaction.getFailureCode() + "\"}");
    }
    
}
