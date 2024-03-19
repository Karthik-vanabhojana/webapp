package com.csye6225.assignment.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuplicateUserNameException extends Exception{
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public DuplicateUserNameException() {
        super("Duplicate User Id and User is already present");
        LOGGER.debug("GlobalExceptionHandler. BadRequestEmail {}");
        LOGGER.error("Duplicate User Id and User is already present");
    }
}
