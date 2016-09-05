package org.nww.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class NwwEurekaServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(NwwEurekaServerApplication.class).web(true).run(args);
    }
}
