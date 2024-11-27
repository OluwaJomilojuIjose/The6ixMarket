package com.example.afinal.model;

public class Message {
    private final long id;
    private final long senderId;
    private final long receiverId;
    private final String messageContent;
    private final String timestamp;

    private String senderName; // Add this field if not already present

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public Message(long senderId, long receiverId, String messageContent) {
        this(-1, senderId, receiverId, messageContent, null);
    }

    public Message(long id, long senderId, long receiverId, String messageContent, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public long getSenderId() { return senderId; }
    public long getReceiverId() { return receiverId; }


    public String getMessageContent() { return messageContent; }
    public String getTimestamp() { return timestamp; }
}
