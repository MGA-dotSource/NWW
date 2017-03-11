/**
 * 
 */
package org.nww.services.web;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.nww.services.TemplateRenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

/**
 * @author mga
 *
 */
public class EmailServiceImpl implements EmailService {
	private static final String HOST = "Host";

	@Autowired
	private MailSender mailSender;
	
	@Resource(name = "TemplateRenderService")
	private TemplateRenderService templateRenderService;
	
	@Value("${nww.host}")
	private String host;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/* (non-Javadoc)
	 * @see org.nww.services.EmailService#getMailSender()
	 */
	@Override
	public MailSender getMailSender() {
		return mailSender;
	}
	
	/* (non-Javadoc)
	 * @see org.nww.services.EmailService#setMailSender(org.springframework.mail.MailSender)
	 */
	@Override
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nww.services.EmailService#sendMail(java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean sendMail(String[] to, String from, String subject, String message) {
		MimeMessage mm = ((JavaMailSender)getMailSender()).createMimeMessage();
		try {
			mm.setHeader( "Content-Transfer-Encoding", "7bit" );
			
			MimeMessageHelper mmh = new MimeMessageHelper(mm);
			mmh.setTo(to);
			mmh.setFrom(from);
			mmh.setSubject(subject);
			mmh.setText(message, true);
			
			((JavaMailSender)getMailSender()).send(mm);
		} catch (MailException | MessagingException e) {
			log.error("Could not send mail from '" + from + "' to '" + StringUtils.join(to, ", ") + "': ", e); 
			return false;
		}
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.services.EmailService#sendMail(java.lang.String[], java.lang.String, java.lang.String, java.lang.String, org.thymeleaf.context.Context)
	 */
	@Override
	public boolean sendMail(String[] to, String from, String subject, String template, Context c) {
		String mailContent = this.templateRenderService.renderTemplate(template, c);
		
		return sendMail(to, from, subject, mailContent);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.nww.services.EmailService#sendMail(java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public boolean sendMail(String[] to, String from, String subject, String template, Map<String, Object> attributes) {
		// add host to attributes if it is not present
		if(!attributes.containsKey(HOST)) {
			attributes.put(HOST, host);
		}
		
		final Context c = new Context();
		
		c.setVariables(attributes);
		
		c.setLocale(Locale.GERMANY);
		
		String mailContent = this.templateRenderService.renderTemplate(template, c);
		
		return sendMail(to, from, subject, mailContent);
	}
}
