package org.example.bankappuserservice.userRegistration.infra.adapter.inbound;

import jakarta.validation.ConstraintViolationException;
import org.example.bankappuserservice.userRegistration.application.exception.CpfAlreadyExistsException;
import org.example.bankappuserservice.userRegistration.application.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CpfAlreadyExistsException.class)
    ResponseEntity<Void> handleCpfAlreadyExists (CpfAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    ResponseEntity<Void> handleEmailAlreadyExists (EmailAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Void> handleViolationException (ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
