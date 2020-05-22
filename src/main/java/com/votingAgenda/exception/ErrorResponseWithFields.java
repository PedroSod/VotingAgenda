package com.votingAgenda.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ErrorResponseWithFields {
    private int errorCode;
    private String error;
    private Collection<String> fieldErrors;

    public ErrorResponseWithFields(HttpStatus status, Collection<String> fieldErrors) {
        this.errorCode = status.value();
        this.error = status.name();
        this.fieldErrors = fieldErrors;
    }

}
