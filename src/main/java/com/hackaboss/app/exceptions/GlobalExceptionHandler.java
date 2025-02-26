package com.hackaboss.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handlerResourceNotFoundException(ResourceNotFoundException ex, WebRequest req){
        ExceptionDTO errorDetails = new ExceptionDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                "El recurso solicitado no existe.",
                null
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDTO> handleBusinessException(BusinessException ex, WebRequest req){
        ExceptionDTO errorDetails = new ExceptionDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Business Error",
                ex.getMessage(),
                req.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGlobalException(Exception ex, WebRequest req){
        ExceptionDTO errorDetails = new ExceptionDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocurrió un error inesperado. Por favor, contacte al soporte técnico",
                req.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest req) {

        String errorMessage = "Formato de parámetro inválido.";

        if (ex.getRequiredType() == LocalDate.class){
            errorMessage = "Formato de fecha inválido. use dd/MM/yyyy (ej. 09/06/2024).";
        }
        ExceptionDTO exceptionDTO = new ExceptionDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                errorMessage,
                req.getDescription(false)
        );
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }
}
