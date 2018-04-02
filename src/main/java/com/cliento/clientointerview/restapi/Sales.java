package com.cliento.clientointerview.restapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Sales {

	public abstract List<Sale> getSales();

    @JsonCreator
    public static Sales create(
            @JsonProperty("sales") List<Sale> sales
    ) {
        return new AutoValue_Sales(sales);
    }
}

