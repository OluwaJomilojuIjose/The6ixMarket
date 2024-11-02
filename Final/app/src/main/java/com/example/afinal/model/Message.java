package com.example.afinal.model;

public class Message {
    private String senderName;
    private String messageContent;

    public Message(String senderName, String messageContent) {
        this.senderName = senderName;
        this.messageContent = messageContent;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessageContent() {
        return messageContent;
    }
}
