/**
 * 
 */
package org.nww.modules.messenger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nww.app.AbstractApplicationController;
import org.nww.modules.messenger.orm.ChatMessage;
import org.nww.modules.messenger.orm.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles all messenger related requests the require rendering a template.
 * @author mga
 */
@Controller
@RequestMapping("/network/messenger")
public class ChatMessageController extends AbstractApplicationController {
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@RequestMapping(value = "/widget/", method = RequestMethod.GET)
	public CompletionStage<String> widget(Model model) {
		return CompletableFuture.supplyAsync(() -> {
			Date now = new Date();
			Pageable pageable = new PageRequest(0, 5, Direction.DESC, "lastModified");
			Page<ChatMessage> firstPage = chatMessageRepository.findByLastModifiedBefore(now, pageable);
			
			// need to create a new page with reversed content
			List<ChatMessage> inversed = new ArrayList<>(firstPage.getContent());
			Collections.reverse(inversed);
			
			model.addAttribute("Messages", new PageImpl<>(inversed, pageable, firstPage.getTotalElements()));
			
			return "messenger/chatWidget";
		});
	}
}
