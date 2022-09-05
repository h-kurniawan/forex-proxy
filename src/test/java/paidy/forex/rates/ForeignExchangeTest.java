package paidy.forex.rates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.Instant;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestClientTest(ForeignExchange.class)
public class ForeignExchangeTest {
    @Autowired
    private MockRestServiceServer server;
   
    @Autowired 
    private ObjectMapper mapper;    
    
    @Autowired
    private ForeignExchange forex;
    
    @BeforeEach
    public void setUp() {
    }
 
    @Test
    public void getRates_Success() throws JsonProcessingException {
        var currencyPair = "USDJPY";
        var rate = new ExchangeRate("USD", "JPY", 0.61F, 0.82F, 0.71F, Instant.parse("2019-01-01T00:00:00.000Z"));
        var respond = mapper.writeValueAsString(List.of(rate));

        server
            .expect(requestTo(String.format("/rates?pair=%s", currencyPair)))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(respond, MediaType.APPLICATION_JSON));

        var returnedRates = forex.getRates(List.of(currencyPair));

        assertThat(returnedRates)
            .isEqualTo(List.of(rate));
    }
}
