package proxy.forex.rates;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyPairConstraintValidator implements ConstraintValidator<CurrencyPairConstraint, List<String>> {
    @Override
    public boolean isValid(List<String> pairs, ConstraintValidatorContext context) {
        for (var pair : pairs) {
            if (pair.length() != 6 || pair != pair.toUpperCase()) {
                return false;
            }
        }

        return true;
    }
}
