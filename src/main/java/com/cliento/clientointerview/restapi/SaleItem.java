package com.cliento.clientointerview.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.math.BigDecimal;

@AutoValue
public abstract class SaleItem  {


    public abstract String getDescription();

    public abstract BigDecimal getPrice();


    @JsonCreator
    public static SaleItem create(
            @JsonProperty("description") String description,
            @JsonProperty("price") BigDecimal price
    ) {
        return new AutoValue_SaleItem(description, price);
    }
}