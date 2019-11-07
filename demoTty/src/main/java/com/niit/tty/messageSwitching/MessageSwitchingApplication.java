package com.niit.tty.messageSwitching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages={"com.niit.tty"})
@EnableSwagger2
public class MessageSwitchingApplication {
	private static final Logger logger = LoggerFactory.getLogger(MessageSwitchingApplication.class);
	  
	public static void main(String[] args) {
		 logger.info("this is a info message");
	      logger.warn("this is a warn message");
	      logger.error("this is a error message");
		SpringApplication.run(MessageSwitchingApplication.class, args);

	}
}