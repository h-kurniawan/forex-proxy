package paidy.forex.rates;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import paidy.forex.configuration.ForexConfiguration;

@Service
@ConfigurationProperties("service.forex")
public class ForeignExchange implements ForeignExchangeService {
    private final RestTemplate restTemplate;
    private final ForexConfiguration forexConfig;

    public ForeignExchange(
        final RestTemplateBuilder builder,
        ForexConfiguration forexConfig) {
        this.restTemplate = builder.build();
        this.forexConfig = forexConfig;
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
        var httpEntity = new HttpEntity<>("body", headers);

        try {
            return restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<ExchangeRate>>() {}
            );
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                .status(ex.getRawStatusCode())
                .body(List.of());
        }
    }
}
