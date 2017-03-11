package org.nww.modules.messenger.events;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.nww.modules.messenger.models.Message;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.nww.services.TemplateRenderService;
import org.nww.services.web.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.IContext;

/**
 * Handles occuring {@link MessageCreatedEvent}
 */
@Component
public class MessageCreatedEventListener implements ApplicationListener<MessageCreatedEvent> {

	private UserManager userManager;
	private TemplateRenderService templateRenderService;
	private EmailService emailService;
	
	@Value("${nww.host}")
	private String host;

	@Autowired
	public MessageCreatedEventListener(final UserManager userManager, 
			final TemplateRenderService templateRenderService,
			final EmailService emailService) {
		this.userManager = userManager;
		this.templateRenderService = templateRenderService;
		this.emailService = emailService;
	}
	
    @Override
    public void onApplicationEvent(MessageCreatedEvent event) {
    	final User sender = event.getMessage().getSender();
    	// find all users that are not the sender
    	final Query q = new Query(Criteria.where("uuid").ne(sender.getUUID()));
    	final List<? extends User> usersWithoutSender = userManager.findAllByQuery(q);
    	
    	// filter all users that have not been notified since 1h
    	Long millisLimes = (new Date()).getTime();
    	List<? extends User> recipients = usersWithoutSender.stream()
    		.filter(u -> getNextNotificationAllowedTimestamp(u, event.getMessage().getConversationId()) < millisLimes)
    		.collect(Collectors.toList());
    	
    	sendNotificationEmail(recipients, event.getMessage());
    }
    
    private Long getNextNotificationAllowedTimestamp(final User u, final String conversationId) {
    	final String lastTimestamp = u.getString("LastMessageNotification_" + conversationId);
    	if(StringUtils.isEmpty(lastTimestamp)) {
    		return 0l;
    	}
    	
    	return Long.valueOf(lastTimestamp);
    }
    
    private void sendNotificationEmail(final List<? extends User> recipients, final Message message) {
		recipients.forEach(u -> {
			CompletableFuture.supplyAsync(() -> {
				// update timestamp
				u.setString("LastMessageNotification_" + message.getConversationId(), Long.toString((new Date()).getTime() + 3600000l));
				userManager.save(u);
				
				// send mail
				final Map<String, Object> model = new HashMap<>();
				model.put("Host", host);
				model.put("Recipient", u);
				model.put("Message", message);
				final IContext ctx = templateRenderService.prepareContext(model);
				
				final String renderedContent = templateRenderService.renderTemplate("messenger/mail/messengerNotificationMail", ctx);
				
				boolean mailSubmitted = emailService.sendMail(
						new String[] { u.getUsername() },
						"info@netzwerkwohnen.org", 
						"Neue Chat Nachrichten auf netzwerkwohnen.org",
						renderedContent);
				return null;
			});
		});
    }
}
