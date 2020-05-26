package com.agendavoting.exception;

public class DuplicateVoteException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public DuplicateVoteException(String cpf) {
        super("the CPF " + cpf + ", has already voted on this agenda.");
    }
}
