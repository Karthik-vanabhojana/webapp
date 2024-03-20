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

        LOGGER.debug("GlobalExceptionHandler. authenticationfailed {} ",ex.getMessage());
        LOGGER.error("Incorrect UserName or password");
        return ResponseEntity.status(401)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)

    public ResponseEntity<Void> badrequest(SQLIntegrityConstraintViolationException ex){
        LOGGER.debug("GlobalExceptionHandler. badrequest {} ",ex.getMessage());

        LOGGER.error("Invalid details entered");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>  handleMethodArgumentNotValidException(MethodArgumentNotValidException methodargumentNotvalid){
        LOGGER.debug("GlobalExceptionHandler. handleMethodArgumentNotValidException {} ",methodargumentNotvalid.getMessage());

        LOGGER.error("Invalid User Details Entered");
        Map<String, String> errorResponse= new HashMap<>();
        methodargumentNotvalid.getBindingResult().getAllErrors().forEach((error)->{
            String FieldName=((FieldError) error).getField();
            String msg=error.getDefaultMessage();
            errorResponse.put(FieldName, msg);
        });
        return new ResponseEntity<Map<String,String>>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)

    public ResponseEntity<Void> methodnotsupported(HttpRequestMethodNotSupportedException ex){
        LOGGER.debug("GlobalExceptionHandler. methodnotsupported {} ",ex.getMessage());

        LOGGER.error("HttpRequestMethodNotSupportedException");
        return ResponseEntity.status(405)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Void> authenticationemailfail(ResourceNotFoundException ex){

        LOGGER.debug("GlobalExceptionHandler. authenticationemailfail {} ",ex.getMessage());

        LOGGER.error("User Not Found");
        return ResponseEntity.status(401)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)

    public ResponseEntity<Void> invalidparams(HttpMessageNotReadableException ex){
        LOGGER.debug("GlobalExceptionHandler. invalidparams {} ",ex.getMessage());
        LOGGER.error("Invalid Parameter provided for userdetails");

        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler({DataAccessResourceFailureException.class})
    public ResponseEntity<Void> dbnotconnect(DataAccessResourceFailureException ex ){

        LOGGER.debug("GlobalExceptionHandler. dbnotconnect {} ",ex.getMessage());

        LOGGER.error("Database Not Connected");
        return ResponseEntity.status(503)
                .cacheControl(CacheControl.noCache())
                .build();
    }

    @ExceptionHandler({CannotCreateTransactionException.class})
    public ResponseEntity<Void> dbnotconnect(CannotCreateTransactionException ex ){

        LOGGER.debug("GlobalExceptionHandler. dbnotconnect {} ",ex.getMessage());

        LOGGER.error("Database Not Connected");
        return ResponseEntity.status(503)
                .cacheControl(CacheControl.noCache())
                .build();
    }
    @ExceptionHandler(BadRequestEmail.class)
    public ResponseEntity<Void> badupdateemailparam(BadRequestEmail ex){

        LOGGER.debug("GlobalExceptionHandler. badupdateemailparam {} ",ex.getMessage());

        LOGGER.error("Bad UserName Format");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }

    @ExceptionHandler(DuplicateUserNameException.class)
    public ResponseEntity<Void> duplicateemail(DuplicateUserNameException ex){
        LOGGER.debug("GlobalExceptionHandler. duplicateemail {} ",ex.getMessage());
        LOGGER.error("Duplicate User Id and User is already present");
        return ResponseEntity.status(400)
                .cacheControl(CacheControl.noCache())
                .build();
    }

}
