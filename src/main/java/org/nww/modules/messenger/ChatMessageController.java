/**
 * 
 */
package org.nww.modules.messenger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nww.app.AbstractApplicationController;
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
	
	@RequestMapping(value = "/widget/", method = RequestMethod.GET)
	public CompletionStage<String> widget(Model model) {
		return CompletableFuture.supplyAsync(() -> {
			return "messenger/chatWidget";
		});
	}
}
