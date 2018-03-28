package com.cliento.clientointerview.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Customer{


    public abstract String getName();

    public abstract String getEmail();

    @JsonCreator
    public static Customer create(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email
    ) {
        return new AutoValue_Customer(name, email);
    }
}
