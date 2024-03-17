package com.csye6225.assignment.webapp;


import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class WebappApplication {



	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("jsonLogger");
		logger.debug("Debug message");
		logger.info("Application Starting..........................");
		SpringApplication.run(WebappApplication.class, args);

		logger.info("Application Started!!!!!!!!!!!!...............");

	}

}
