package com.costcook.exceptions;

public class InvalidProviderException extends RuntimeException {
    public InvalidProviderException(String message) {
        super(message);
    }
}