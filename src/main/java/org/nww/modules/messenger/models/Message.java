package org.nww.modules.messenger.models;

import org.nww.core.utils.ApplicationContextHelper;
import org.nww.modules.users.orm.User;
import org.nww.modules.users.orm.UserManager;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Represents a single chat message within a conversation.
 * Conversations represent themselves by having a unique identifier to be used each time
 * a new message should be added.
 */
@Document(collection = "messages")
public class Message implements Comparable<Message> {
    @Id
    private String id;
    @Indexed
    private String conversationId;
    @Indexed
    private String senderUuid;
    private Content content;
    @CreatedDate
    private Date creationDate;
    @LastModifiedDate
    private Date lastModified;
    
    @Transient
	private User sender;
    @Transient
	private UserManager userManager = ApplicationContextHelper.getBeanByClass(UserManager.class);

    /**
     * Public constructor only for (de-)serialization purposes.
     */
    public Message() { }

    /**
     * Private constructor.
     * @param conversationId the conversation id
     * @param sender the sender
     * @param content the content
     */
    private Message(final String conversationId, final String senderUuid, final Content content) {
        this.conversationId = conversationId;
        this.senderUuid = senderUuid;
        this.content = content;
    }

    /**
     * Create a new {@link Message} instance.
     * @param conversationId the conversation id
     * @param sender the message sender
     * @param content the message content
     * @return new instance
     */
    public static Message of(final String conversationId, final String senderUuid, final Content content) {
        return new Message(conversationId, senderUuid, content);
    }

    /**
     * @return the message id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the conversation id
     */
    public String getConversationId() {
        return conversationId;
    }

    public String getSenderUuid() {
		return senderUuid;
	}
    
    /**
     * @return the message sender
     */
    public User getSender() {
    	if(null == this.sender) {
    		this.sender = userManager.findOne(this.senderUuid);
    	}
        return this.sender;
    }

    /**
     * @return the message content
     */
    public Content getContent() {
        return content;
    }

    /**
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the last modified date
     */
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public int compareTo(Message o) {
        return creationDate.compareTo(o.getCreationDate());
    }
}
