package org.nww.modules.frontend;

import javax.validation.Valid;

import org.nww.app.AbstractApplicationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.IContext;

/**
 * The home controller is responsible of handling all requests from the public available frontend.
 * @author mga
 *
 */
@Controller
@RequestMapping(value = "/")
public class FrontendController extends AbstractApplicationController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * Generate a new instance.
	 */
	public FrontendController() {
		super();
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String landingPage(Model model) {
		ContactForm form = new ContactForm();
		
		model.addAttribute("ContactForm", form);
		model.addAttribute("IsLandingPage", true);
		model.addAttribute("BodyClass", "landing-page");
		
		return "frontend/landingpage";
	}
	
	@RequestMapping(value = "/imprint", method = RequestMethod.GET)
	public String imprint(Model model) {
		model.addAttribute("BodyClass", "landing-page");
		return "frontend/imprint";
	}
	
	@RequestMapping(value = "/dataprotection", method = RequestMethod.GET)
	public String dataprotection(Model model) {
		model.addAttribute("BodyClass", "landing-page");
		return "frontend/dataprotection";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute("BodyClass", "gray-bg");
		return "frontend/login";
	}
	
	@RequestMapping(value = "/sendContact.do", method = RequestMethod.POST)
	public ResponseEntity<String> sendContactDo(@Valid @ModelAttribute("ContactForm") ContactForm form,
			BindingResult bindingResult, Model model) {
		
		if(!bindingResult.hasErrors()) {
			log.debug("Binding result has no errors. Trying to send mail:");
			
			IContext ctx = getTemplateRenderService().prepareContext(model.asMap());
			
			String renderedContent = getTemplateRenderService().renderTemplate("frontend/mail/contactEmail", ctx);
			
			boolean mailSubmitted = getEmailService().sendMail(new String[] { populateContactEmail() },
					form.getEmail(), 
					"Kontaktanfrage von " + form.getName(),
					renderedContent);
			
			if(mailSubmitted) {
				log.debug("Mail submitted succesfully to '" + populateContactEmail() + "'!");
				return ResponseEntity.ok("");
			}
		}
		
		log.debug("Binding result has errors:", bindingResult);
		return ResponseEntity.badRequest().body("");
	}
	
	/**
	 * Redirect the old about page to the new single page about section.
	 * @return the redirect
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String redirectOldAboutPage() {
		return "redirect:/#about";
	}
	
	/**
	 * Redirect the old contact and register pages to the new single page contact form.
	 * There is no dedicated register page currently
	 * @return the redirect
	 */
	@RequestMapping(value = { "/contact", "/register" })
	public String redirectOldContactPage() {
		return "redirect:/#contact";
	}
}	