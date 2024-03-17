package com.csye6225.assignment.webapp.Authentication;

import com.csye6225.assignment.webapp.dto.UserDTO;
import com.csye6225.assignment.webapp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthenticationManagement {
    @Autowired
    private UserService userservice;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private static Logger logger = LoggerFactory.getLogger("jsonLogger");
    public boolean checklogin(String authorizationHeader) {
        logger.trace("AuthenticationManagement checklogin{}");
        logger.info("Checking login credential");

        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        String[] parts = credentials.split(":", 2);
        String email = parts[0];
        String password = parts[1];
        UserDTO userDto = userservice.getuser(email);

        if (passwordEncoder.matches(password, userDto.getPassword())) {
            logger.info("Credential for user with username "+email + "matched sucessfully");
            logger.info("Authentication Sucessfull");

            return true;
        } else {
            logger.info("Credential for user with username "+email + " is not matched sucessfully");
            logger.info("Authentication Unsucessfull");

            return false;
        }
    }

    public String getEmail(String authorizationHeader) {
        String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        String[] parts = credentials.split(":", 2);
        String email = parts[0];
        return email;

    }

    public boolean isDatabaseConnected() {
        return userservice.checkDatabaseConnection();
    }
}
