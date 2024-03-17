package com.csye6225.assignment.webapp.annotation;

import com.csye6225.assignment.webapp.Authentication.AuthenticationManagement;
import com.csye6225.assignment.webapp.exception.AuthenticationException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SecuredAuthenticationAspect {

    @Autowired
    private AuthenticationManagement authenticationManagement;
    private static Logger logger = LoggerFactory.getLogger("jsonLogger");

    @Before("@annotation(com.csye6225.assignment.webapp.annotation.Secured)")
    public void authenticateBeforeApi() throws AuthenticationException {
        logger.trace("SecuredAuthenticationAspect authenticateBeforeApi{} ");
        logger.info("Authenticating user ");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            if (!authenticationManagement.checklogin(authorizationHeader)) {
                logger.error("Authentication failed ");

                throw new AuthenticationException();
            }
        }
        else {
            logger.error("Authentication failed ");
            throw new AuthenticationException();
            }

    }
}
