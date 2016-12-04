/**
 * 
 */
package org.nww.modules.messenger.orm;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
	public Page<ChatMessage> findBySenderUUID(String senderUUID, Pageable pageable);
	
	public List<ChatMessage> findByLastModifiedAfter(Date timestamp);
	public Page<ChatMessage> findByLastModifiedAfter(Date timestamp, Pageable p);
	
	public Page<ChatMessage> findByLastModifiedBefore(Date timestamps, Pageable p);
	
}
