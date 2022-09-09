package paidy.forex.rates;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import paidy.forex.configuration.ForexConfiguration;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForeignExchange implements ForeignExchangeService {
    private final RestTemplate restTemplate;
    private final ForexConfiguration forexConfig;
    private final ForexCache forexCache;


    public ForeignExchange(
        final RestTemplateBuilder builder,
        ForexConfiguration forexConfig,
        ForexCache forexCache) {
        this.restTemplate = builder.build();
        this.forexConfig = forexConfig;
        this.forexCache = forexCache;
    }

    @Override
    public ResponseEntity<List<ExchangeRate>> getRates(List<String> currencyPairs) {
        var uriBuilder = UriComponentsBuilder
            .fromHttpUrl(forexConfig.getBaseUri().toString())
            .path("/rates");
        currencyPairs.stream().forEach((pair) -> 
            uriBuilder.queryParam("pair", pair));
        var uriComponents = uriBuilder.build();

        var headers = new HttpHeaders();
        headers.add("token", forexConfig.getAccessToken());
        var httpEntity = new HttpEntity<>(headers);

        try {
            var response =  restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<ExchangeRate>>() {}
            );
            cacheExchageRates(response.getBody());

            return response;
        } catch (HttpStatusCodeException ex) {
            var exchangeRates = getExchangeRateFromCache(currencyPairs);
            var statusCode = exchangeRates.isEmpty() ? ex.getStatusCode():  HttpStatus.OK;

            return ResponseEntity
                    .status(statusCode)
                    .body(exchangeRates);
        }
    }

    private void cacheExchageRates(List<ExchangeRate> exchangeRates) {
        exchangeRates.stream().forEach(rate -> {
            var key = rate.getFromCurrency() + rate.getToCurrency();
            forexCache.set(key, rate);
        });
    }

    private List<ExchangeRate> getExchangeRateFromCache(List<String> currencyPairs) {
        var exchangeRates = new ArrayList<ExchangeRate>();
        for (var pair: currencyPairs) {
            var rate = forexCache.<ExchangeRate>get(pair);
            if (rate == null) {
                return List.of();
            }
            exchangeRates.add(rate);
        }

        return  exchangeRates;
    }
}
