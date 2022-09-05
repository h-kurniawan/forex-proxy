package paidy.forex.rates;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface ForeignExchangeService {
    ResponseEntity<List<ExchangeRate>> getRates(List<String> currencyPairs);
}
