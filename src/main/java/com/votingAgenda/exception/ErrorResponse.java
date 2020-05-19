package com.votingAgenda.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int errorCode;
    private String error;
    private String message;
    private Collection<String> fieldErrors;

    public ErrorResponse(HttpStatus status, String message) {
        this.errorCode = status.value();
        this.error = status.name();
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, Collection<String> fieldErrors) {
        this.errorCode = status.value();
        this.error = status.name();
        this.fieldErrors = fieldErrors;
    }

}
