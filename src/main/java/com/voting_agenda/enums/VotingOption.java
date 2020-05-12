package com.voting_agenda.enums;

public enum VotingOption {

    YES("sim"), NO("não");

    private String description;

    VotingOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
