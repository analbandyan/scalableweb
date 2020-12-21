package com.waes.scalableweb.exception.exceptions;

public class CalculationRequestNotFoundException extends RuntimeException {

    public CalculationRequestNotFoundException(String id) {
        super("Calculation request for id = " + id + " not found.");
    }


}
