package com.cactus.oauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cactus.springsecurity.client", "com.cactus.oauthserver"})
public class OauthAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthAuthorizationServerApplication.class, args);
	}

}
