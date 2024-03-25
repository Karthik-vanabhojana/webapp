package com.csye6225.assignment.webapp;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication

public class WebappApplication {



	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("jsonLogger");
		logger.info("Application Starting..........................");

		SpringApplication.run(WebappApplication.class, args);

		logger.info("Application Started!!!!!!!!!!!!...............");
//		logger.info(generateToken("karikvtla@gmail.com)"));

	}
//	private static String generateToken(String userId) {
//		String SECRET_KEY = "yourhardcodedsecretkeyaddingnewvalues12345678901234";
//
//		Date now = new Date();
//		Date expiryDate = new Date(now.getTime() + 120000); // 2 minutes
//
//		return Jwts.builder()
//				.setSubject(String.valueOf(userId))
//				.setIssuedAt(now)
//				.setExpiration(expiryDate)
//				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//				.setSubject("value@gmail.com")
//				.compact();
//	}

}
