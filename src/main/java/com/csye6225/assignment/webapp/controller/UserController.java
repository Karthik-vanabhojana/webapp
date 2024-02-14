package com.csye6225.assignment.webapp.controller;

import com.csye6225.assignment.webapp.Authentication.AuthenticationManagement;
import com.csye6225.assignment.webapp.annotation.Secured;
import com.csye6225.assignment.webapp.dto.CreateValid;
import com.csye6225.assignment.webapp.dto.UpdateValid;
import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.exception.BadRequestEmail;
import com.csye6225.assignment.webapp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userservice;
    @Autowired
    private AuthenticationManagement authenticationManagement;
    @RequestMapping(value = {"/healthz","/v1/user","/v1/user/self"},method = {RequestMethod.OPTIONS,RequestMethod.HEAD})
    public ResponseEntity<Void> checkAdditionalfield() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .cacheControl(CacheControl.noCache())
                .build();
    }

    @RequestMapping(value = "/healthz",method = RequestMethod.GET)

    public ResponseEntity<Void>  healthCheck(HttpServletRequest httpRequest) {

        if(!httpRequest.getMethod().toLowerCase().equals("get")){
            return ResponseEntity.status(405)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
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
        if (authenticationManagement.isDatabaseConnected()) {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noCache())
                    .build();
        } else {
            return ResponseEntity.status(503)
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
    }
    @PostMapping("/v1/user")
    public ResponseEntity<UserDTO> createUser(@Validated(CreateValid.class) @RequestBody UserDTO userdto) {
        UserDTO registeredUser = this.userservice.registerUser(userdto);
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(registeredUser);

    }

    @Secured
    @GetMapping("/v1/user/self")
    public ResponseEntity<UserDTO> getself(@RequestHeader("Authorization") String authorizationHeader,HttpServletRequest httpRequest) throws IOException {
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

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(userDTO);

    }

    @Secured
    @PutMapping("/v1/user/self")
    public ResponseEntity<Void> updateSelf(@RequestHeader("Authorization") String authorizationHeader, @Validated(UpdateValid.class) @RequestBody UserDTO userdto) throws BadRequestEmail, IOException {
        if(!getNonnullParam(userdto).contains("username")){
            UserDTO userDTO = userservice.updateUser(userdto, authenticationManagement.getEmail(authorizationHeader));

            return ResponseEntity.noContent()
                    .cacheControl(CacheControl.noCache())
                    .build();
        }
        else {
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
                    System.out.println(field.getName());
                }
            }
        } catch (IllegalAccessException e) {
        }
        return nonNull;
    }
}
