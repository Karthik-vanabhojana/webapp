package com.csye6225.assignment.webapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller

public class BasicErrorController  implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<Void> handleError(){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/404")
    public void handle404(){
    }
}
