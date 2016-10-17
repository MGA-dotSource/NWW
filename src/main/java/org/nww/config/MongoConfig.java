/**
 * 
 */
package org.nww.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

@Configuration
public class MongoConfig {
	
	@Autowired
	List<Converter<?, ?>> converters = new ArrayList<>();
	
	@Bean
	public CustomConversions customConverters() {
		
		return new CustomConversions(converters);
	}
}
