package com.busbooking.exceptions;

public class InvalidSeatException extends RuntimeException {

    public InvalidSeatException(String message) {
        super(message);
    }
}