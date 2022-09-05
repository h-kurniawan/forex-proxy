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
    private float bid;
    private float ask;
    private float price;
    private Instant timeStamp;
}
