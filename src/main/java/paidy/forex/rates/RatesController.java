package paidy.forex.rates;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rates")
public class RatesController {
    private final ForeignExchange forex;

    @GetMapping
    ResponseEntity<List<ExchangeRate>> getRates() throws JsonProcessingException {
        var rates = forex.getRates();
        return ResponseEntity.ok(rates);
    }
}
