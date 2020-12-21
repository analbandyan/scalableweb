package com.waes.scalableweb.exception.exceptions;

public class DiffRequestAlreadyExistsException extends RuntimeException {

    public DiffRequestAlreadyExistsException(String id) {
        super("Diff request for id = '" + id + "' already exists.");
    }

}
