package paidy.forex.rates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rates")
@Validated
public class RatesController {
    private final ForeignExchange forex;

    @GetMapping
    ResponseEntity<List<ExchangeRate>> getRates(
            @RequestParam("pair")
            @CurrencyPairConstraint
            List<String> currencyPairs) {
        var response = forex.getRates(currencyPairs);
        return ResponseEntity.ok(response.getBody());
    }
}