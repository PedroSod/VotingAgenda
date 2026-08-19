package com.agendavoting.exception;

import java.io.Serial;

public class UnableToVoteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3244236110056980710L;

    public UnableToVoteException(String cpf) {
        super("The cpf " + cpf + " is unable to vote.");
    }
}
