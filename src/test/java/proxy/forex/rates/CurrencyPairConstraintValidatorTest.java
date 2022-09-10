package proxy.forex.rates;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CurrencyPairConstraintValidatorTest {
    private CurrencyPairConstraintValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CurrencyPairConstraintValidator();
    }

    @Test
    public void isValid() {
        var isValid = validator.isValid(List.of("USDJPY"), null);
        assertThat(isValid).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"USDJP", "USDJPYN", "usdjpy"})
    public void isNotValid(String pair) {
        var isValid = validator.isValid(List.of("pair"), null);
        assertThat(isValid).isFalse();
    }
}
