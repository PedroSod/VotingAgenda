package com.agendavoting.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ErrorResponse {
    private int errorCode;
    private String error;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.errorCode = status.value();
        this.error = status.name();
        this.message = message;
    }


}
