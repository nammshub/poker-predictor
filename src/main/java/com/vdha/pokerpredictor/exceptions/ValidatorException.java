package com.vdha.pokerpredictor.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidatorException extends Exception {
    List<String> errors;

    public ValidatorException(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return String.join(System.lineSeparator(), errors);
    }
}
