package org.nww.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NwwUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NwwUserServiceApplication.class, args);
	}
}
