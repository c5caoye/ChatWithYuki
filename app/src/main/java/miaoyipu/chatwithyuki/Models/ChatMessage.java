package miaoyipu.chatwithyuki.Models;

import java.util.Date;

/**
 * Created by cy804 on 2017-07-24.
 * In order to store the chat messages in the Firebase real-time databases, must create a model for them.
 * To make it compatible with FirebaseUI, must also add a default constructor.
 */

public class ChatMessage {
    private String msgText, authorName, from, to;
    private long msgTime;

    /**
     *
     * @param text message text
     * @param author author of the message
     * @param from author uid
     * @param to target uid
     */
    public ChatMessage(String text, String author, String from, String to) {
        this.msgText = text;
        this.authorName = author;
        this.from = from;
        this.to = to;
        this.msgTime = new Date().getTime();
    }

    /**
     * Default constructor to make class compatible with FirebaseUI.
     */
    public ChatMessage() {}

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
