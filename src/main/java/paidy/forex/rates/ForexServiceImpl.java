package paidy.forex.rates;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import paidy.forex.configuration.ForexConfiguration;

@Slf4j
@Service
public class ForexServiceImpl implements ForexService {
    private final RestTemplate restTemplate;
    private final ForexConfiguration forexConfig;
    private final ForexCache forexCache;

    public ForexServiceImpl(
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

        log.error("token is: " + forexConfig.getAccessToken());


        try {
            var response = restTemplate.exchange(
                    uriComponents.toUriString(),
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<List<ExchangeRate>>() {
                    }
            );
            cacheExchageRates(response.getBody());

            return response;
        } catch (Exception ex) {

log.error("oh no exception!!!");
log.error(ex.getMessage());

            var exchangeRates = getExchangeRateFromCache(currencyPairs);
            HttpStatus httpStatus;
            if (!exchangeRates.isEmpty()) {
                httpStatus = HttpStatus.OK;
            }
            else if (ex instanceof HttpStatusCodeException) {
                httpStatus = ((HttpStatusCodeException)ex).getStatusCode();
            }
            else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return ResponseEntity
                    .status(httpStatus)
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
        for (var pair : currencyPairs) {
            var rate = forexCache.<ExchangeRate>get(pair);
            if (rate == null) {
                return List.of();
            }
            exchangeRates.add(rate);
        }

        return exchangeRates;
    }    
}
