package pl.kurs.finaltest.validations.pesel;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeselValidator implements ConstraintValidator<Pesel, String> {


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return s.matches("\\d{11}");
    }

    @Override
    public void initialize(Pesel constraintAnnotation) {
    }
}
