package com.example.identityfamily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityFamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityFamilyApplication.class, args);
	}

}
