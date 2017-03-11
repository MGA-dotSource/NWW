package org.nww.modules.messenger.models;

/**
 * Represents the content part of a {@link Message}.
 */
public class Content {
    private String text;

    /**
     * Public default constructor only needed for (de-)serialization purposes.
     */
    public Content() { }

    /**
     * Private constructor.
     * @param text the text part of the content
     */
    private Content(String text) {
        this.text = text;
    }

    /**
     * Create a new {@link Content} instance.
     * @param text the text content part
     * @return new {@link Content} instance
     */
    public static Content of(String text) {
        return new Content(text);
    }

    /**
     * @return the text part of the content
     */
    public String getText() {
        return text;
    }
}
