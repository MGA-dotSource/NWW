package org.nww.ui_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NwwUiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NwwUiBackendApplication.class, args);
	}
}
