package com.agendavoting.enums;

import lombok.Getter;

@Getter
public enum VotingOption {

    YES("yes"), NO("no");

    private final String description;

    VotingOption(String description) {
        this.description = description;
    }

}
