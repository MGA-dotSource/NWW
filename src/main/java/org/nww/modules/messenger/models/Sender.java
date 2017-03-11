package org.nww.modules.messenger.models;

/**
 * Represents the smallest amount of user data needed to handle the
 * sender of a message.
 */
public class Sender {
    private String userId;
    private String userName;
    private String displayName;

    /**
     * Default public constructor for (de-)serialization purposes
     */
    public Sender() { }

    /**
     * private constructor
     * @param userId the senders user userId
     * @param userName the senders username
     * @param displayName the senders display name
     */
    private Sender(String userId, String userName, String displayName) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
    }

    /**
     * Create a new sender.
     * @param userId the sender userId
     * @param userName the senders username
     * @param displayName the senders display name
     * @return a new {@link Sender} instance
     */
    public static Sender of(String userId, String userName, String displayName) {
        return new Sender(userId, userName, displayName);
    }

    /**
     * @return the senders user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the senders username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the senders display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        // implement this
        return super.equals(obj);
    }
}
