package org.nww.modules.messenger.models;

/**
 * A representation of the draft that needs to be send to the service in order
 * to create a new message.
 * No setters as this is only used via serialization.
 */
public class MessageDraft {
    private String senderId;
    private String senderName;
    private String senderDisplayName;
    private String contentText;

    /**
     * @return the sender Id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * @return the sender name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * @return the senders display name
     */
    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    /**
     * @return the textual content of the message
     */
    public String getContentText() {
        return contentText;
    }
}
