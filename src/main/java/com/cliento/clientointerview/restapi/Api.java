package com.cliento.clientointerview.restapi;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
import com.cliento.clientointerview.cardreader.SaleService;
import com.cliento.clientointerview.jdbc.CashRegisterStorage;

@RestController
@EnableAutoConfiguration
public class Api {

	final static Logger logger = Logger.getLogger(Api.class);
	
    private final CardReader cardReader;
    private final CashRegisterStorage cashRegisterStorage;
    private final SaleService saleService;

    @Autowired
    public Api(CardReader cardReader, CashRegisterStorage cashRegisterStorage, SaleService saleService) {
        this.cardReader = cardReader;
        this.cashRegisterStorage = cashRegisterStorage;
        this.saleService = saleService;
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
    
    @PostMapping(path="/sale", produces="application/json")
    public ResponseEntity<?> sale(@RequestBody Sale sale) {
    	final CountDownLatch cardTransactionLatch = new CountDownLatch(1);
    	
    	CardTransaction cardTransaction = new CardTransaction(cardTransactionLatch);
		cardReader.startCardTransaction(sale.getTotalAmount(), cardTransaction);
		
		try {
			if(!cardTransactionLatch.await(10L, TimeUnit.SECONDS)) {
				logger.info("com.cliento.clintointerview.restapi.Api.sale: CardTransaction timeout reached");
				return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("{\"message\":\"Transaction timeout reached\"}");
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
			cardTransaction.setFailureCode(e.getMessage());
		}
		
		Optional<SaleResponse> optional = cardTransaction.isSuccess() ? saleService.doSale(sale, cardTransaction) : Optional.empty();
		
    	return optional.isPresent() ? ResponseEntity.ok(optional.get()) :
    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"" + cardTransaction.getFailureCode() + "\"}");
    }
    
}
