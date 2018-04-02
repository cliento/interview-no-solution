package com.cliento.clientointerview.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SaleResponse {

	public abstract Long getSaleId();

    public abstract String getTransactionRef();

    @JsonCreator
    public static SaleResponse create(
            @JsonProperty("saleId") Long saleId,
            @JsonProperty("transactionRef") String transactionRef
    ) {
        return new AutoValue_SaleResponse(saleId, transactionRef);
    }
}
