package com.jeonghyeong.healthchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
public class SimpleHttpHealthCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleHttpHealthCheckerApplication.class, args);
	}

}
