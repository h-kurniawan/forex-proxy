package paidy.forex.rates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.time.Instant;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

import paidy.forex.configuration.ForexConfiguration;

@RunWith(SpringRunner.class)
@RestClientTest(ForeignExchange.class)
@EnableConfigurationProperties(value = ForexConfiguration.class)
@TestPropertySource("classpath:application-test.properties")
public class ForeignExchangeTest {
    @Autowired
    private MockRestServiceServer server;
   
    @Autowired 
    private ObjectMapper mapper;    
    
    @Autowired
    private ForeignExchange forex;

    @Test
    public void getRates_Success() throws Exception {
        // arrange
        var currencyPair = "EURJPY";
        var rate = new ExchangeRate("EUR", "JPY", 0.68F, 0.95F, 0.02F, Instant.parse("2022-09-05T15:33:20.941Z"));
        var respond = mapper.writeValueAsString(List.of(rate));

        server
            .expect(requestTo(String.format("http://forex.com/rates?pair=%s", currencyPair)))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("token", "secure-token"))
            .andRespond(withSuccess(respond, MediaType.APPLICATION_JSON));

        // act
        var returnedRates = forex.getRates(List.of(currencyPair));

        // assert
        assertThat(returnedRates.getBody())
            .isEqualTo(List.of(rate));
    }

    @Test
    public void getRates_Fail() throws Exception {
        // arrange
        var currencyPair = "EURJPY";

        server
            .expect(requestTo(String.format("http://forex.com/rates?pair=%s", currencyPair)))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header("token", "secure-token"))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // act
        var returnedRates = forex.getRates(List.of(currencyPair));

        // assert
        assertThat(returnedRates.getBody())
            .isEqualTo(List.of());
    }}
