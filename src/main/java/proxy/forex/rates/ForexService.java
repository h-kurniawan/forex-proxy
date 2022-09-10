package proxy.forex.rates;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ForexService {
    ResponseEntity<List<ExchangeRate>> getRates(List<String> currencyPairs);
}
