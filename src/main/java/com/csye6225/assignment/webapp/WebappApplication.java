package com.csye6225.assignment.webapp;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication

public class WebappApplication {


	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("jsonLogger");
		logger.info("Application Starting..........................");
		SpringApplication.run(WebappApplication.class, args);
		logger.info("Application Started!!!!!!!!!!!!...............");


	}

}
