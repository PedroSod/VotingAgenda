package com.voting_agenda.exception;

public class VotingClosedException extends RuntimeException {

    private static final long serialVersionUID = 3244236110056980710L;

    public VotingClosedException(String id) {
        super("The voting session " + id + " has ended");
    }
}
