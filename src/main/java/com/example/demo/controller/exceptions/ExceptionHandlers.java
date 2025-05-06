package com.example.demo.controller.exceptions;

import com.example.demo.service.exception.BookException;
import com.example.demo.service.exception.DataBaseException;
import com.example.demo.service.exception.ResourceNotFoundException;
import com.example.demo.service.exception.StockException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> notFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        StandardException exception = new StandardException(Instant.now(), HttpStatus.NOT_FOUND.value(),"Resource not found",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardException> dataBaseException(DataBaseException ex, HttpServletRequest request) {
        StandardException exception = new StandardException(Instant.now(), HttpStatus.BAD_REQUEST.value(),"Database exception",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(BookException.class)
    public ResponseEntity<StandardException> bookException(BookException ex, HttpServletRequest request) {
        StandardException exception = new StandardException(Instant.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(),"Failed to manage the Book",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardException> bookException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        StandardException exception = new StandardException(Instant.now(), HttpStatus.BAD_REQUEST.value(),"Insert a valid value",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(StockException.class)
    public ResponseEntity<StandardException> bookException(StockException ex, HttpServletRequest request) {
        StandardException exception = new StandardException(Instant.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(),"Failed to manage the Stock",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception);
    }
}
