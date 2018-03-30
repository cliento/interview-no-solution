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
import com.cliento.clientointerview.cardreader.CardTransactionCallback;
import com.cliento.clientointerview.jdbc.CashRegisterStorage;

@RestController
@EnableAutoConfiguration
public class Api {

	final static Logger logger = Logger.getLogger(Api.class);
	
	private CardTransactionCallback cardTransactionCallback;
	private final CountDownLatch cardTransactionLatch = new CountDownLatch (1);

    private final CardReader cardReader;
    private final CashRegisterStorage cashRegisterStorage;

    @Autowired
    public Api(CardReader cardReader, CashRegisterStorage cashRegisterStorage) {
        this.cardReader = cardReader;
        this.cashRegisterStorage = cashRegisterStorage;
    }

    @GetMapping(path="/hello-world")
    public ResponseEntity<?> helloWorld(){
    	logger.info("in helloWorld()");
        return ResponseEntity.ok("Hello World!");
    }
    
    @PostMapping(path="/sale")
    public ResponseEntity<?> sale(@RequestBody Sale sale) {
    	logger.info("in sale() " + sale);
    	
//    	CompletableFuture<List<Sale>> compFuture = new CompletableFuture<List<Sale>>();
//    	CompletableFuture<CardTransaction> compFuture = CompletableFuture.supplyAsync(() -> {
//	    	CardTransaction cardTransaction = new CardTransaction();
//    		cardReader.startCardTransaction(sale.getTotalAmount(), cardTransaction);
//    		return cardTransaction;
//    	}).thenApply((cardTransaction) -> {return doSale(sale, cardTransaction);});

//    	CompletableFuture comFut = new CompletableFuture<String>
    	CardTransaction cardTransaction = new CardTransaction(sale, cashRegisterStorage);
		cardReader.startCardTransaction(sale.getTotalAmount(), cardTransaction);
		
		while (cardTransaction.getFailureCode() == null && cardTransaction.getSuccessCode() == null) {
			
		}
		return cardTransaction.isSuccess() ? ResponseEntity.ok(cardTransaction.getSuccessCode()) : new ResponseEntity<>(cardTransaction.getFailureCode(), HttpStatus.BAD_REQUEST);
//		CompletableFuture comFut = new CompletableFuture<Long>();
		
//    	CompletableFuture.supplyAsync(() -> {
//    		cardTransaction.isCompleted();
//    	}
//    	).thenApply(doSale(sale, cardTransaction));
    	
//    	try {
//    		CardTransaction res = compFuture.get();
//    		return cardTransaction.isSuccess() ? ResponseEntity.ok(cardTransaction.getSuccessCode()) : new ResponseEntity<>(cardTransaction.getFailureCode(), HttpStatus.BAD_REQUEST);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
//			return ResponseEntity.ok(cardTransaction.getSuccessCode());
//		} else {
//			return new ResponseEntity<>(cardTransaction.getFailureCode(), HttpStatus.BAD_REQUEST);
//		}
    }
    
    private void doSale(Sale sale, CardTransaction cardTransaction) {
    	if(cardTransaction.isSuccess()) {
    		long customerId = cashRegisterStorage.createCustomer(sale.getCustomer().getName(), sale.getCustomer().getEmail());
    		cashRegisterStorage.createSale(customerId, cardTransaction.getSuccessCode(), sale.getItems());
    	}
//    	return cardTransaction;
    }
    
    private void testCode(Sale sale) {
    	long last = 0L;
    	int x = 0;
    	while(x < 3) {
    		Long customerId = cashRegisterStorage.createCustomer(sale.getCustomer().getName(), sale.getCustomer().getEmail());
    		last = customerId;
    		System.out.println("Response is: " + customerId);
    		x++;
    	}
//    	long res = crs.createSale(customerId, sale.getCardTransactionRef(), sale.getItems());
    	
    	long res = cashRegisterStorage.createSale(last, "ref1", sale.getItems());
    	System.out.println("Result is: " + res);
    	
    	List<Sale> sales = cashRegisterStorage.getSales();
    	System.out.println("Number of sales: " + sales.size());
    	for (Sale s : sales) {
			System.out.println(s.getCardTransactionRef());
			System.out.println(s.getCustomer().getName());
			System.out.println(s.getCustomer().getEmail());
			
			s.getItems().stream()
				.forEach(si -> { 
					System.out.println(si.getDescription());
					System.out.println(si.getPrice());
				});
		}
    }
    
}
