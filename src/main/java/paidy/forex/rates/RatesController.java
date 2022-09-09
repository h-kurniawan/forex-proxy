package paidy.forex.rates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

        var statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            var errorMsg = switch (response.getStatusCode()) {
                case TOO_MANY_REQUESTS -> {
                    yield "Number of requests limit has been reached. Please try again later.";
                }
                default -> {
                    yield "An error has occurred retrieving exchange rate.";
                }
            };

            throw new ResponseStatusException(statusCode, errorMsg);
        }

        return response;
    }
}
