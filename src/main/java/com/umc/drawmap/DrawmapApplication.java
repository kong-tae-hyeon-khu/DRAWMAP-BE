package com.umc.drawmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DrawmapApplication {

	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true"); // S3 설정
	}
	public static void main(String[] args) {
		SpringApplication.run(DrawmapApplication.class, args);
	}
}
