package com.openclassroom.mddapi.exceptions;

public class MddBadCredentialsException extends RuntimeException {
    public MddBadCredentialsException(String message) {
        super(message);
    }
}
