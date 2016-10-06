/**
 * 
 */
package org.nww.app;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.nww.services.TemplateRenderService;
import org.nww.services.web.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

/**
 * Provides basic methods for controllers.
 * @author mga
 *
 */
@Controller
public abstract class AbstractApplicationController extends org.springframework.web.servlet.mvc.AbstractController {
	
	protected static final String REDIRECT_TO_NETWORK = "redirect:/network/";
	
	@Value("${nww.version}")
	private String version;
	@Value("${nww.host}")
	private String host;
	@Value("${nww.mail.contact}")
	private String contactMail;
	@Value("${nww.tracking.piwik.enabled}")
	private boolean piwikEnabled;
	@Value("${nww.tour.enabled}")
	private boolean tourEnabled;
	
	@Autowired
	private UserManager userMgr;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TemplateRenderService templateRenderService;

	/**
	 * @return the user repository
	 */
	public UserManager getUserManager() {
		return this.userMgr;
	}
	
	/**
	 * Get the email service used by this controller to submit emails.
	 * @return the email service
	 */
	public EmailService getEmailService() {
		return this.emailService;
	}
	
	/**
	 * @return the template render service
	 */
	public TemplateRenderService getTemplateRenderService() {
		return templateRenderService;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return null;
	}
	
	
	@ModelAttribute(value = "Version")
	public String populateVersion() {
		return this.version;
	}
	
	@ModelAttribute(value = "Host")
	public String populateHost() {
		return this.host;
	}
	
	@ModelAttribute(value = "ContactEmail")
	public String populateContactEmail() {
		return this.contactMail;
	}
	
	@ModelAttribute(value = "CurrentUser")
	public User populateCurrentUser() {
		return getUserManager().findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	@ModelAttribute(value = "PiwikTrackingEnabled")
	public boolean populatePiwikTrackingEnabled() {
		return this.piwikEnabled;
	}
	
	@ModelAttribute(value = "TourEnabled")
	public boolean populateTourEnabled() {
		return this.tourEnabled;
	}
	
	protected CompletionStage<User> withCurrentUser(String userName) {
		return CompletableFuture.supplyAsync(() -> {
			return getUserManager().findByUsername(userName);
		});
	}
}
