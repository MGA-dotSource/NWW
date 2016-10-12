/**
 * 
 */
package org.nww.modules.messenger.orm;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	public Page<ChatMessage> findBySenderUUID(String senderUUID, Pageable pageable);
	
	public Page<ChatMessage> findByLastModifiedAfter(Date lastReqested, Pageable p);
}
