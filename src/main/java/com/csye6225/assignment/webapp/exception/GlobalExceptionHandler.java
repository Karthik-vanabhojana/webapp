package com.csye6225.assignment.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> authenticationfailed(AuthenticationException ex){

        LOGGER.trace("GlobalExceptionHandler. AuthenticationException {} ",ex.getMessage());
        LOGGER.error("Email Not accepted during update details");
        return ResponseEntity.status(401)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Void> authenticationfailed(SQLIntegrityConstraintViolationException ex){
        LOGGER.trace("GlobalExceptionHandler. SQLIntegrityConstraintViolationException {} ",ex.getErrorCode());
        LOGGER.error("SQL Exception for unique details");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handleMethodArgumentNotValidException(MethodArgumentNotValidException methodargumentNotvalid){
        LOGGER.trace("GlobalExceptionHandler. MethodArgumentNotValidException {} ",methodargumentNotvalid.getMessage());
        LOGGER.error("API Method Not allowed Exception");
        Map<String, String> errorResponse= new HashMap<>();
        methodargumentNotvalid.getBindingResult().getAllErrors().forEach((error)->{
            String FieldName=((FieldError) error).getField();
            String msg=error.getDefaultMessage();
            errorResponse.put(FieldName, msg);
        });
        return new ResponseEntity<Map<String,String>>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Void> authenticationfailed(HttpRequestMethodNotSupportedException ex){
        LOGGER.trace("GlobalExceptionHandler. HttpRequestMethodNotSupportedException {} ",ex.getMessage());
        LOGGER.error("API Method Not allowed Exception");
        return ResponseEntity.status(405)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> authenticationemailfail(ResourceNotFoundException ex){
        LOGGER.trace("GlobalExceptionHandler. ResourceNotFoundException {} ",ex.getMessage());
        LOGGER.error("API endpoint not available Exception");
        return ResponseEntity.status(401)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Void> authenticationWrongrequest(HttpMessageNotReadableException ex){
        LOGGER.trace("GlobalExceptionHandler. HttpMessageNotReadableException {} ",ex.getMessage());
        LOGGER.error("Invalid Parameter gor the field");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler({DataAccessResourceFailureException.class})
    public ResponseEntity<Void> dbnotconnect(DataAccessResourceFailureException ex ){
        LOGGER.trace("GlobalExceptionHandler. DataAccessResourceFailureException {} ",ex.getMessage());
        LOGGER.error("Database Not Connected");
        return ResponseEntity.status(503)
                .cacheControl(CacheControl.noCache())
                .build();
    }

    @ExceptionHandler({CannotCreateTransactionException.class})
    public ResponseEntity<Void> dbnotconnect(CannotCreateTransactionException ex ){
        LOGGER.trace("GlobalExceptionHandler. CannotCreateTransactionException {} ",ex.getMessage());
        LOGGER.error("Database Not Connected");
        return ResponseEntity.status(503)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(BadRequestEmail.class)
    public ResponseEntity<Void> badupdateemailparam(BadRequestEmail ex){
        LOGGER.trace("GlobalExceptionHandler. BadRequestEmail {} ",ex.getMessage());
        LOGGER.error("Bad Email Format");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }

}
