package paidy.forex.rates;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rates")
public class RatesController {
    private final ForeignExchange forex;

    @GetMapping
    ResponseEntity<List<ExchangeRate>> getRates(@RequestParam("pair") List<String> currencyPairs) {
        var response = forex.getRates(currencyPairs);
        return ResponseEntity.ok(response.getBody());
    }
}
