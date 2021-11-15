package com.stockx.publishmanagerws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PublishManagerWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublishManagerWsApplication.class, args);
	}

}
