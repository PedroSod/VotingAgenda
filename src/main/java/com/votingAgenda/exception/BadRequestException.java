package com.votingAgenda.exception;

public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public BadRequestException(String message) {
        super(message);
    }
}
