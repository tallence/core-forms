package com.tallence.formeditor.validator;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.tallence.formeditor.validator.NumberValidator.MESSAGE_KEY_NUMBERFIELD_MAX;
import static com.tallence.formeditor.validator.NumberValidator.MESSAGE_KEY_NUMBERFIELD_MIN;
import static org.junit.Assert.assertEquals;

public class NumberValidatorTest {

    NumberValidator numberValidator;

    @Before
    public void init() {
        numberValidator = new NumberValidator();
    }

    @Test
    public void testWithValidString() {
        numberValidator.setMinSize(1);
        numberValidator.setMaxSize(10);
        assertEquals(0, numberValidator.validate("1").size());
    }

    @Test
    public void testWithNumberTooSmall() {
        numberValidator.setMinSize(1);
        final List<ValidationFieldError> validationErrors = numberValidator.validate("0");
        assertEquals(1, validationErrors.size());
        assertEquals(MESSAGE_KEY_NUMBERFIELD_MIN, validationErrors.get(0).getMessageKey());
    }

    @Test
    public void testWithNumberTooBig() {
        numberValidator.setMaxSize(10);

        List<ValidationFieldError> validationErrors = numberValidator.validate("11");
        assertEquals(1, validationErrors.size());
        assertEquals(MESSAGE_KEY_NUMBERFIELD_MAX, validationErrors.get(0).getMessageKey());

        // the value of 10000000000 is bigger than Integer.MAX_VALUE
        validationErrors = numberValidator.validate("10000000000");
        assertEquals(1, validationErrors.size());
        assertEquals(MESSAGE_KEY_NUMBERFIELD_MAX, validationErrors.get(0).getMessageKey());
    }

    @Test
    public void testWithBigIntValid() {
        numberValidator.setMinSize(1);

        // the value of 10000000000 is bigger than Integer.MAX_VALUE
        assertEquals(0, numberValidator.validate("10000000000").size());
    }

}
