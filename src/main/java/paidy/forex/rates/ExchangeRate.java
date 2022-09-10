package paidy.forex.rates;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ExchangeRate {
    @JsonProperty("from")
    private String fromCurrency;
    @JsonProperty("to")
    private String toCurrency;
    private double bid;
    private double ask;
    private double price;
    private Instant timeStamp;
}
