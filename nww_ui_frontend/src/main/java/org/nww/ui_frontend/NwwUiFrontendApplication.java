package org.nww.ui_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NwwUiFrontendApplication {

	public static void main(String[] args) {

	    SpringApplication.run(NwwUiFrontendApplication.class, args);
	}
}
