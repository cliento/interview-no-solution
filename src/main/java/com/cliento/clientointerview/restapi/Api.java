package com.cliento.clientointerview.restapi;

import com.cliento.clientointerview.cardreader.CardReader;
import com.cliento.clientointerview.jdbc.CashRegisterStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Api {

    private final CardReader cardReader;
    private final CashRegisterStorage cashRegisterStorage;

    @Autowired
    public Api(CardReader cardReader, CashRegisterStorage cashRegisterStorage) {
        this.cardReader = cardReader;
        this.cashRegisterStorage = cashRegisterStorage;
    }

    @GetMapping(path="/hello-world")
    ResponseEntity helloWorld(){
        return ResponseEntity.ok("Hello World!");
    }

}
