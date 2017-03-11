package org.nww.api;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.nww.modules.messenger.data.MessageRepository;
import org.nww.modules.messenger.events.MessageCreatedEvent;
import org.nww.modules.messenger.models.Content;
import org.nww.modules.messenger.models.Message;
import org.nww.services.web.HTMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/messenger/actions")
public class MessengerActionEndpoint {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private HTMLService htmlService;
    
    @RequestMapping(value = "/add.do", method = POST)
    public ResponseEntity<String> doAdd(
            @RequestParam String conversationId,
            @RequestParam String userId,
            @RequestParam String message) {

        Message m = Message.of(
                conversationId,
                userId,
                Content.of(htmlService.convertNewlinesToHTML(message.trim())));

        messageRepository.save(m);

        //messageCreatedEventListener.onApplicationEvent(new MessageCreatedEvent(this,  m));
        applicationEventPublisher.publishEvent(new MessageCreatedEvent(this, m));
        

        return ResponseEntity.ok().body("");
    }
}
