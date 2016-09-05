package org.nww.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NwwSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NwwSearchServiceApplication.class, args);
	}
}
