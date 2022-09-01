package paidy.forex.rates;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
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
