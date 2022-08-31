package paidy.forex.rates;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ForeignExchange implements ForeignExchangeService {
    @Override
    public List<ExchangeRate> getRates() {
        var rate = new ExchangeRate("USD", "JPY", 0.61F, 0.82F, 0.71F, Instant.now());
        return List.of(rate);
    }
}
