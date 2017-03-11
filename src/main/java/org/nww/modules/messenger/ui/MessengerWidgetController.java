package org.nww.modules.messenger.ui;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.nww.modules.messenger.data.MessageRepository;
import org.nww.modules.messenger.models.Message;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.nww.services.data.SortingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/messenger/widget")
public class MessengerWidgetController {

    private static final Integer PAGE_SIZE = 10;
    private static final Sort DEFAULT_SORTING = new Sort(DESC, "creationDate");
    private static final Sort UPDATE_SORTING = new Sort(ASC, "creationDate");
    private static final String WIDGET_CONVERSATION_ID = "main";

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserManager userManager;
    
    private SortingHelper<Message> messageSortingHelper = new SortingHelper<>();

    @RequestMapping(value = "/", method = GET, params = { "uid"})
    public CompletionStage<String> showWidget(
            @RequestParam(name = "uid") final String userId,
            final Model model) {
        return CompletableFuture.supplyAsync(() -> {
        	
    		final User user = userManager.findOne(userId);
        	final Pageable pageable = new PageRequest(0, PAGE_SIZE, DEFAULT_SORTING);
            final Date originDate = new Date();
            final Page<Message> newestMessages = messageRepository.findByConversationIdEqualsAndCreationDateBefore(WIDGET_CONVERSATION_ID, originDate, pageable);

            model.addAttribute("Messages", newestMessages);
            model.addAttribute("ReversedMessages", messageSortingHelper.reverse(newestMessages.getContent()));
            model.addAttribute("OriginDate", originDate);

            model.addAttribute("User", user);

            return "messenger/widget";
        });
    }

    @RequestMapping(value = "/{originDate}/{pageNumber}/", method = GET)
    public CompletionStage<String> pageWidget(
            @PathVariable final Long originDate,
            @PathVariable final Integer pageNumber,
            final Model model) {
        return CompletableFuture.supplyAsync(() -> {

            final Pageable pageable = new PageRequest(pageNumber, PAGE_SIZE, DEFAULT_SORTING);
            final Date passedOriginDate = new Date(originDate);
            final Page<Message> messagePage = messageRepository.findByConversationIdEqualsAndCreationDateBefore(WIDGET_CONVERSATION_ID, passedOriginDate, pageable);

            model.addAttribute("Messages", messagePage);
            model.addAttribute("ReversedMessages", messageSortingHelper.reverse(messagePage.getContent()));
            model.addAttribute("OriginDate", passedOriginDate);

            return "messenger/messagePage";
        });
    }

    @RequestMapping(value = "/{originDate}/updates/", method = GET)
    public CompletionStage<String> updateWidget(
            @PathVariable Long originDate,
            Model model) {
        return CompletableFuture.supplyAsync(() -> {

            final Pageable pageable = new PageRequest(0, PAGE_SIZE, UPDATE_SORTING);
            final Date passedOriginDate = new Date(originDate);
            final Page<Message> messagePage = messageRepository.findByConversationIdEqualsAndCreationDateAfter(WIDGET_CONVERSATION_ID, passedOriginDate, pageable);

            model.addAttribute("Messages", messagePage);
            model.addAttribute("ReversedMessages", messageSortingHelper.reverse(messagePage.getContent()));

            // if there are message found then we set the next origin date to the creation date of the latest message found
            if(messagePage.hasContent()) {
                Date nextOriginDate = messagePage.getContent().get(messagePage.getContent().size() - 1).getCreationDate();
                model.addAttribute("OriginDate", nextOriginDate);
            }
            else {
                model.addAttribute("OriginDate", passedOriginDate);
            }

            return "messenger/messageUpdates";
        });
    }
}
