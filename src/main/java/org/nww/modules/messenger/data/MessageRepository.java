package org.nww.modules.messenger.data;

import org.nww.modules.messenger.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface MessageRepository extends MongoRepository<Message, String> {
    /**
     * Find a number of messages created before the passed date.
     * @param conversationId the conversation id
     * @param before the date to check against
     * @param pageable allows settings a maximum number of messages to be retrieved
     * @return page of messages
     */
    Page<Message> findByConversationIdEqualsAndCreationDateBefore(String conversationId, Date before, Pageable pageable);

    /**
     * Find a number of messages created after the passed date.
     * @param conversationId the conversation id
     * @param after the date to check against
     * @param pageable allows setting a maximum number of messages to be retrieved
     * @return page of messages
     */
    Page<Message> findByConversationIdEqualsAndCreationDateAfter(String conversationId, Date after, Pageable pageable);
}
