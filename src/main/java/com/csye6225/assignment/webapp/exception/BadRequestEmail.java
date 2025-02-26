package com.csye6225.assignment.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadRequestEmail extends Exception  {
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    public BadRequestEmail() {
        super("NotAuthorized");
        LOGGER.debug("GlobalExceptionHandler. BadRequestEmail {}");
        LOGGER.error("UserName Not accepted during update details");
    }
}
