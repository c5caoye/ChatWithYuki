package miaoyipu.chatwithyuki.Models;

import java.util.Date;

/**
 * Created by cy804 on 2017-08-01.
 */

public class GroupMessage {
    private String msgText, authorName, from;
    private long msgTime;

    public GroupMessage() {}

    /**
     *
     * @param text message text
     * @param author author display name
     * @param from author id
     */
    public GroupMessage(String text, String author, String from) {
        this.msgText = text;
        this.authorName = author;
        this.from = from;
        this.msgTime = new Date().getTime();
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }
}
