package com.cliento.clientointerview.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

import java.math.BigDecimal;
import java.util.List;

@AutoValue
public abstract class Sale {

    @Nullable
    public abstract String getCardTransactionRef();
    public abstract Customer getCustomer();
    public abstract List< SaleItem> getItems();

    @JsonCreator
    public static Sale create(
            @Nullable String cardTransactionRef,
            @JsonProperty("customer") Customer customer,
            @JsonProperty("items") List<SaleItem> items) {
        return new AutoValue_Sale(cardTransactionRef, customer, items);
    }

    public BigDecimal getTotalAmount(){
        return getItems().stream().map(SaleItem::getPrice).reduce(BigDecimal::add).get();
    }
}
