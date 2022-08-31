package paidy.forex.rates;

import java.time.Instant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExchangeRate {
    private String fromCurrency;
    private String toCurrency;
    private float bid;
    private float ask;
    private float price;
    private Instant timestamp;
}
