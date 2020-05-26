package com.agendavoting.exception;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public RecordNotFoundException(String id) {
        super("No record found for id : " + id);
    }
}
