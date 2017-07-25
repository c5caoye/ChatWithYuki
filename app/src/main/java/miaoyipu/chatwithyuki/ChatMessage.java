package miaoyipu.chatwithyuki;

import java.util.Date;

/**
 * Created by cy804 on 2017-07-24.
 * In order to store the chat messages in the Firebase real-time databases, must create a model for them.
 * To make it compatible with FirebaseUI, must also add a default constructor.
 */

public class ChatMessage {
    private String msgText, msgUser, uid;
    private long msgTime;

    public ChatMessage(String text, String user, String uid) {
        this.msgText = text;
        this.msgUser = user;
        this.uid = uid;
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

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
