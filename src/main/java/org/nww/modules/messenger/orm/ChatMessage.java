/**
 * 
 */
package org.nww.modules.messenger.orm;

import org.nww.core.data.AbstractPersistentObject;
import org.nww.modules.users.orm.User;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
public class ChatMessage extends AbstractPersistentObject {
	private String senderUUID;
	private String content;
	
	private ChatMessage(String senderUUID, String content) {
		this.senderUUID = senderUUID;
		this.content = content;
	}
	
	public static ChatMessage of(User sender, String content) {
		return new ChatMessage(sender.getUUID(), content);
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
}
