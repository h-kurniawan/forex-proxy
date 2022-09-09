package paidy.forex.rates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(RatesController.class)
public class RatesControllerTest {
    @MockBean
    private ForeignExchange forex;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<List<ExchangeRate>> jsonRates;
    
    @Test
    public void getRates() throws Exception {
        // given
        List<ExchangeRate> rates = List.of(
            new ExchangeRate("USD", "JPY", 0.61F, 0.82F, 0.71F, Instant.parse("2019-01-01T00:00:00.000Z"))
        );
        var expectedResponse = new ResponseEntity<>(rates, HttpStatus.OK);
        given(forex.getRates(List.of("USDJPY")))
            .willReturn(expectedResponse);

        // when
        var response = mvc
            .perform(get("/rates?pair=USDJPY"))
            .andReturn()
            .getResponse();
        
        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
            jsonRates.write(rates).getJson()
        );
    }
}
