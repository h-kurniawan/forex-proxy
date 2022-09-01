package paidy.forex.rates;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest
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
        given(forex.getRates())
            .willReturn(rates);

        // when
        var response = mvc
            .perform(get("/rates"))
            .andReturn()
            .getResponse();
        
        // then
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        then(response.getContentAsString()).isEqualTo(
            jsonRates.write(rates).getJson()
        );
    }
}
