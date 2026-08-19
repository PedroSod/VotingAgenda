package com.agendavoting.exception;

import java.io.Serial;

public class VotingClosedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3244236110056980710L;

    public VotingClosedException(String id) {
        super("The voting session " + id + " has ended.");
    }
}
