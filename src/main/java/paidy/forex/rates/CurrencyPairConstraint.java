package paidy.forex.rates;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = CurrencyPairConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyPairConstraint {
    String message() default "Currency pair must be a concatenation of two different currency codes and in upper case e.g. USDJPY";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}