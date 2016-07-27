/**
 * 
 */
package org.nww.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author mga
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] NETWORK_USERS = new String[] { "REGISTERED_USER", "ADMIN" };
	private static final String[] ADMIN_USERS = new String[] { "ADMIN" };
	
	/**
	 * 
	 */
	public SecurityConfig() {
	}

	/**
	 * @param disableDefaults
	 */
	public SecurityConfig(boolean disableDefaults) {
		super(disableDefaults);
	}

	@Autowired
	MongoAuthenticationProvider mongoAuthenticationProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.mongoAuthenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/register.do").permitAll()
			.antMatchers("/files/upload.do").hasAnyRole(NETWORK_USERS)
			.antMatchers("/files/**").permitAll()
			.antMatchers("/error").permitAll()
			.antMatchers("/network/**").hasAnyRole(NETWORK_USERS)
			.antMatchers("/admin/**").hasAnyRole(ADMIN_USERS)
			.and()
				.formLogin().loginPage("/login").defaultSuccessUrl("/network").failureUrl("/login?error=f")
				.usernameParameter("username").passwordParameter("password")
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.exceptionHandling().accessDeniedPage("/login?error=ad")
			.and()
				.csrf();
	}
}
