package com.votingAgenda.enums;

public enum VotingOption {

    YES("yes"), NO("no");

    private String description;

    VotingOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}