/**
 * 
 */
package org.nww;

import org.nww.modules.suppliers.orm.SupplierController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;

/**
 * The NetzwerkWohnen Spring Boot Application
 * @author mga
 *
 */
@SpringBootApplication
@ComponentScan("org.nww")
@EnableScheduling
@EnableMongoAuditing
public class NetzwerkWohnenApp extends WebMvcConfigurerAdapter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
		
		SpringApplication.run(NetzwerkWohnenApp.class, args);
	}
	
	/**
	 * Override this to disable automatic decoding of URL parts to allow a "/" beeing encoded and correctly passed to 
	 * the handling method as a path variable (see {@link SupplierController}).
	 */
	@Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setUrlDecode(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
}
