package org.nww.modules.messenger.events;

import org.nww.modules.messenger.models.Message;
import org.springframework.context.ApplicationEvent;

/**
 * Event indicating a newly created message.
 */
public class MessageCreatedEvent extends ApplicationEvent {

    /**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -6915590687153608037L;
	
	private Message message;

    /**
     * Create a new MessageCreatedEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     * @param message the created message
     */
    public MessageCreatedEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }

    /**
     * @return the created message
     */
    public Message getMessage() {
        return message;
    }
}
