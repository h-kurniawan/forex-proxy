package paidy.forex.rates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import paidy.forex.configuration.ForexConfiguration;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForeignExchangeTest {
    private ForeignExchange forex;
    private ForexConfiguration forexConfig;

    @Mock
    private ForexCache forexCache;
    @Mock
    private RestTemplateBuilder restTemplateBuilder;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() throws Exception {
        forexConfig = new ForexConfiguration();
        forexConfig.setBaseUri(new URI("http://forex.com"));
        forexConfig.setAccessToken("secure-token");

        Mockito.when(restTemplateBuilder.build())
                .thenReturn(restTemplate);
        forex = new ForeignExchange(restTemplateBuilder, forexConfig, forexCache);
    }

    @Test
    public void getRates_Success() {
        // arrange
        var currencyPairs = List.of("EURJPY", "USDJPY");
        var eurJpy = new ExchangeRate("EUR", "JPY", 0.68F, 0.95F, 0.02F, Instant.parse("2022-09-05T15:33:20.941Z"));
        var usdJpy = new ExchangeRate("USD", "JPY", 0.73F, 0.84F, 0.93F, Instant.parse("2022-09-05T16:34:21.942Z"));
        var responseEntity = new ResponseEntity(List.of(eurJpy, usdJpy), HttpStatus.OK);
        var httpEntity = setupHeader();
        var requestUri = String.format("http://forex.com/rates?pair=%s&pair=%s", currencyPairs.get(0), currencyPairs.get(1));

        doReturn(responseEntity)
                .when(restTemplate)
                .exchange(
                        ArgumentMatchers.eq(requestUri),
                        ArgumentMatchers.eq(HttpMethod.GET),
                        ArgumentMatchers.eq(httpEntity),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRate>>>any()
                );

        // act
        var response = forex.getRates(currencyPairs);

        // assert
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(List.of(eurJpy, usdJpy));

        verify(forexCache, times(1)).set("EURJPY", eurJpy);
        verify(forexCache, times(1)).set("USDJPY", usdJpy);
    }

    @Test
    public void getRates_Success_EmptyResponse() {
        // arrange
        var currencyPairs = List.of("EURJPY", "USDJPY");
        var responseEntity = new ResponseEntity(List.of(), HttpStatus.OK);
        var httpEntity = setupHeader();
        var requestUri = String.format("http://forex.com/rates?pair=%s&pair=%s", currencyPairs.get(0), currencyPairs.get(1));

        doReturn(responseEntity)
                .when(restTemplate)
                .exchange(
                        ArgumentMatchers.eq(requestUri),
                        ArgumentMatchers.eq(HttpMethod.GET),
                        ArgumentMatchers.eq(httpEntity),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRate>>>any()
                );

        // act
        var response = forex.getRates(currencyPairs);

        // assert
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(List.of());

        verify(forexCache, never()).set(any(), any());
    }

    @Test
    public void getRates_Success_ReadFromCache() {
        // arrange
        var currencyPairs = List.of("EURJPY", "USDJPY");
        var eurJpy = new ExchangeRate("EUR", "JPY", 0.68F, 0.95F, 0.02F, Instant.parse("2022-09-05T15:33:20.941Z"));
        var usdJpy = new ExchangeRate("USD", "JPY", 0.73F, 0.84F, 0.93F, Instant.parse("2022-09-05T16:34:21.942Z"));

        when(forexCache.get("EURJPY"))
                .thenReturn(eurJpy);
        when(forexCache.get("USDJPY"))
                .thenReturn(usdJpy);

        var exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

        doThrow(exception)
                .when(restTemplate)
                .exchange(
                        ArgumentMatchers.<String>any(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRate>>>any()
                );

        // act
        var response = forex.getRates(currencyPairs);

        // assert
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(List.of(eurJpy, usdJpy));
    }

    @Test
    public void getRates_Fail() {
        // arrange
        var exception = new HttpServerErrorException(HttpStatus.TOO_MANY_REQUESTS);

        doThrow(exception)
                .when(restTemplate)
                .exchange(
                        ArgumentMatchers.<String>any(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRate>>>any()
                );

        // act
        var response = forex.getRates(List.of("EURJPY"));

        // assert
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(response.getBody())
                .isEqualTo(List.of());
    }

    private HttpEntity setupHeader() {
        var headers = new HttpHeaders();
        headers.add("token", forexConfig.getAccessToken());
        return new HttpEntity<>(headers);
    }
}
