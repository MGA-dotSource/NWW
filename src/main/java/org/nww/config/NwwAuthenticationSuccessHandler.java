package org.nww.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

public class NwwAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final String targetUrl;
	
	private NwwAuthenticationSuccessHandler(final String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
	public static NwwAuthenticationSuccessHandler of(String targetUrl) {
		return new NwwAuthenticationSuccessHandler(targetUrl);
	}
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

}
