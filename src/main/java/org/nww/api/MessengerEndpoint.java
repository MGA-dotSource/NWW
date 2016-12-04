/**
 * 
 */
package org.nww.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nww.modules.messenger.orm.ChatMessage;
import org.nww.modules.messenger.orm.ChatMessageRepository;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.nww.services.web.HTMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mga
 */
@RestController
@RequestMapping("/api/messenger")
public class MessengerEndpoint {
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@Autowired
	private UserManager userMgr;
	
	@Autowired
	private HTMLService htmlService;
	
	@RequestMapping(method = RequestMethod.POST)
	public CompletionStage<ChatMessage> createMessage(
			@RequestParam String userId,
			@RequestParam String content) {
		return CompletableFuture.supplyAsync(() -> {
			User u = userMgr.findOne(userId);
			
			return chatMessageRepository.save(ChatMessage.of(u, htmlService.convertNewlinesToHTML(content)));
		});
	}
}
