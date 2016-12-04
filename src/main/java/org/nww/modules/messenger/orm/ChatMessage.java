/**
 * 
 */
package org.nww.modules.messenger.orm;

import java.util.Date;

import org.nww.core.data.AbstractPersistentObject;
import org.nww.modules.users.orm.User;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
public class ChatMessage extends AbstractPersistentObject {

	private String senderUUID;
	private String content;
	@Transient
	private User sender;
	
	private ChatMessage(User sender, String content) {
		this.senderUUID = sender.getUUID();
		this.sender = sender;
		this.content = content;
	}
	
	public static ChatMessage of(User sender, String content) {
		return new ChatMessage(sender, content);
	}
	
	protected static ChatMessage of(String uuid, User sender, String content, Date lastModified) {
		ChatMessage msg = new ChatMessage(sender, content);
		
		msg.setUUID(uuid);
		msg.setLastModified(lastModified);
		
		return msg;
	}
	
	/**
	 * @return the senderUUID
	 */
	public String getSenderUUID() {
		return senderUUID;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * @return the sender
	 */
	public User getSender() {
		return sender;
	}
}
