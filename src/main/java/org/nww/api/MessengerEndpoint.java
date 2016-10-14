/**
 * 
 */
package org.nww.api;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nww.modules.messenger.orm.ChatMessage;
import org.nww.modules.messenger.orm.ChatMessageRepository;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManagerImpl;
import org.nww.services.web.HTMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
	private UserManagerImpl userMgr;
	
	@Autowired
	private HTMLService htmlService;
	
	@RequestMapping(method = RequestMethod.GET)
	public CompletionStage<Page<ChatMessage>> getMessages(
			@RequestParam(defaultValue = "0", required = false) Long lastRequest,
			@PageableDefault(size = 5, sort = "lastModified", direction = Direction.DESC) Pageable p) {
		return CompletableFuture.supplyAsync(() -> {
			return chatMessageRepository.findByLastModifiedAfter(Date.from(Instant.ofEpochMilli(lastRequest)), p);
		});
	}
	
	@RequestMapping(method = RequestMethod.GET, params = "id")
	public ChatMessage getById(
			@RequestParam("id") ChatMessage message) {
		return message;
	}
	
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
