package com.votingAgenda.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse(status, ex.getBindingResult().getFieldErrors().stream()
                .map(field -> String.format("%s - %s", field.getField(), field.getDefaultMessage())).collect(Collectors.toList())), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String msg =  ExceptionUtils.getRootCauseMessage(ex.getMostSpecificCause()).split("\n")[0];
        return new ResponseEntity<>(new ErrorResponse(status, msg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleRecordNotFoundException(RecordNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public final ResponseEntity<ErrorResponse> handleDuplicateVoteException(DuplicateVoteException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VotingClosedException.class)
    public final ResponseEntity<ErrorResponse> handleVotingClosedException(VotingClosedException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnableToVoteException.class)
    public final ResponseEntity<ErrorResponse> handleUnableToVoteExceptionException(UnableToVoteException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
