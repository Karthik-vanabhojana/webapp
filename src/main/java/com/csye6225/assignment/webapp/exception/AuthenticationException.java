package com.csye6225.assignment.webapp.exception;

public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super("NotAuthorized");
    }
}
