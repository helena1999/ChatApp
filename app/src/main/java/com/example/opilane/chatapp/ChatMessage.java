package com.example.opilane.chatapp;

import java.util.Date;

/**
 * Created by opilane on 18.04.2018.
 */

public class ChatMessage {

    private String messageText;  //sõnumi tekstiks
    private String messageUser;  //sõnumi saajaks
    private long messageTime;    //sõnumi ajaks

    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
