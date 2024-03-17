package com.csye6225.assignment.webapp.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    String resourceName;
    String fieldName;
    long fieldValue;
    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {

        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;

        LOGGER.trace("GlobalExceptionHandler. ResourceNotFoundException {} ");
        LOGGER.error("User with user Id Not found");
    }

    String fieldsValue;
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldsValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldsValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldsValue = fieldsValue;
        LOGGER.trace("GlobalExceptionHandler. ResourceNotFoundException {} ");
        LOGGER.error("User with user Id Not found");
    }

}
