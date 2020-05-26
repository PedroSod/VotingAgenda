package com.agendavoting.exception;

public class UnableToVoteException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public UnableToVoteException(String cpf) {
        super("The cpf " + cpf + " is unable to vote.");
    }
}
