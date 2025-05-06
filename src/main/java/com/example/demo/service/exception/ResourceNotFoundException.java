package com.example.demo.service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super("Id not found: "+id);
    }
}
