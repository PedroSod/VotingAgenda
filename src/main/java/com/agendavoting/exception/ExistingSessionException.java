package com.agendavoting.exception;

import java.io.Serial;

public class ExistingSessionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3244236110056980710L;

    public ExistingSessionException() {
        super("There is already a session for this agenda.");
    }
}
