package paidy.forex.rates;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CurrencyPairConstraintValidatorTest {
    @Test
    public void isValid() {
        var validator = new CurrencyPairConstraintValidator();
        var isValid = validator.isValid(List.of("USDJPY"), null);
        assertThat(isValid).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"USDJP", "USDJPYN", "usdjpy"})
    public void isNotValid(String pair) {
        var validator = new CurrencyPairConstraintValidator();
        var isValid = validator.isValid(List.of("pair"), null);
        assertThat(isValid).isFalse();
    }
}
