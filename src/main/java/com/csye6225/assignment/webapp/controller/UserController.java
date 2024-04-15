package com.csye6225.assignment.webapp.controller;

import com.csye6225.assignment.webapp.Authentication.AuthenticationManagement;
import com.csye6225.assignment.webapp.annotation.Secured;
import com.csye6225.assignment.webapp.dto.CreateValid;
import com.csye6225.assignment.webapp.dto.UpdateValid;
import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.exception.DuplicateUserNameException;
import com.csye6225.assignment.webapp.exception.InvalidToken;
import com.csye6225.assignment.webapp.exception.UserNotverified;
import com.csye6225.assignment.webapp.service.UserService;

import com.csye6225.assignment.webapp.service.impl.Publish;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

@Autowired
Publish publish;
    @Autowired
    private UserService userservice;
    @Autowired
    private AuthenticationManagement authenticationManagement;
    private static Logger LOGGER = LoggerFactory.getLogger("jsonLogger");
    @RequestMapping(value = {"/healthz","/v2/user","/v2/user/self"},method = {RequestMethod.OPTIONS,RequestMethod.HEAD})
    public ResponseEntity<Void> checkAdditionalfield() {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .cacheControl(CacheControl.noCache())
                .build();
    }

    @RequestMapping(value = "/healthz",method = RequestMethod.GET)

    public ResponseEntity<Void>  healthCheck(HttpServletRequest httpRequest) {
        LOGGER.debug("UserController. healthCheck {}");
        LOGGER.info("Checking database Connection");
        LOGGER.debug("Checking for if the API call is GET");

        if(!httpRequest.getMethod().toLowerCase().equals("get")){
            return ResponseEntity.status(405)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        LOGGER.debug("Checking the body is empty");
        if(httpRequest.getContentLength()>-1 ) {
            return ResponseEntity.badRequest()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        LOGGER.debug("Checking the no query is passed");

        if (httpRequest.getQueryString() != null ) {
            return ResponseEntity.badRequest()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        LOGGER.debug("Checking the connection");

        if (authenticationManagement.isDatabaseConnected()) {
            LOGGER.info("connection sucessfull");

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noCache())
                    .build();
        } else {
            LOGGER.error("connection failed");

            return ResponseEntity.status(503)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
    }
    @PostMapping("/v2/user")
    public ResponseEntity<UserDTO> createUser(@Validated(CreateValid.class) @RequestBody UserDTO userdto) throws DuplicateUserNameException {
        LOGGER.debug("UserController. createUser {}");
        LOGGER.info("Trying to Register new User to the database");
        UserDTO registeredUser = this.userservice.registerUser(userdto);

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        LOGGER.info("Sucessfully register user with user name: "+registeredUser.getUsername());
        try {
            publish.publishWithErrorHandlerExample(userdto.getUsername(),userdto.getFirst_name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(registeredUser);
    }

    @Secured
    @GetMapping("/v2/user/self")

    public ResponseEntity<UserDTO> getself(@RequestHeader("Authorization") String authorizationHeader,HttpServletRequest httpRequest) throws IOException, UserNotverified {
        LOGGER.debug("UserController. getself {}");
        LOGGER.info("Getting the details for user after authentication");

        if(httpRequest.getContentLength()>-1 ) {
            return ResponseEntity.badRequest()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }

        if (httpRequest.getQueryString() != null ) {
            return ResponseEntity.badRequest()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        UserDTO userDTO = userservice.getuserByEmail(authenticationManagement.getEmail(authorizationHeader));
        LOGGER.info("User details found for user with username "+userDTO.getUsername());

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(userDTO);

    }

    @Secured
    @PutMapping("/v2/user/self")

    public ResponseEntity<Void> updateSelf(@RequestHeader("Authorization") String authorizationHeader, @Validated(UpdateValid.class) @RequestBody UserDTO userdto) throws BadRequestEmail, IOException, UserNotverified {
        LOGGER.debug("UserController. updateSelf {}");
        LOGGER.info("Updating the details for user after authentication");
        if(!getNonnullParam(userdto).contains("username")){
            UserDTO userDTO = userservice.updateUser(userdto, authenticationManagement.getEmail(authorizationHeader));
            LOGGER.info("User details updated for user with username "+userDTO.getUsername());

            return ResponseEntity.noContent()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        else {
            LOGGER.error("UserName passed with the payload");

            throw new BadRequestEmail();
        }
    }
    private static List<String> getNonnullParam(UserDTO userDto) {
        List<String> nonNull = new ArrayList<>();
        try {
            Class<?> clazz = userDto.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(userDto);
                if (value != null) {
                    nonNull.add(field.getName());
                }
            }
        } catch (IllegalAccessException e) {
        }
        return nonNull;
    }


    @GetMapping("/v2/user/verify")
    public ResponseEntity<String> getVerified(@RequestParam String token, @RequestParam String email) throws UserNotverified, InvalidToken {


        if(userservice.getVerified(token,email)){
            LOGGER.info("Verification sucessful");
            return ResponseEntity.status(HttpStatus.OK).body("Successfully Verified. You can Now View the details on the self");



        }
        else{
            LOGGER.error("Session Ended");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Secrets Expired");

        }


    }
}
