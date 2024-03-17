package com.csye6225.assignment.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationException extends Exception {
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");


    public AuthenticationException() {
        super("NotAuthorized");
        LOGGER.trace("GlobalExceptionHandler. AuthenticationException {}");
        LOGGER.error("Login Authentication Failed");

    }
}
