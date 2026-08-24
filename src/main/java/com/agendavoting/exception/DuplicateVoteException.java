package com.agendavoting.exception;

import java.io.Serial;

public class DuplicateVoteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3244236110056980710L;

    public DuplicateVoteException(String cpf) {
        super("the CPF " + cpf + ", has already voted on this agenda.");
    }
}
