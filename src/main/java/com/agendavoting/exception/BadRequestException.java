package com.agendavoting.exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3244236110056980710L;

    public BadRequestException(String message) {
        super(message);
    }
}
