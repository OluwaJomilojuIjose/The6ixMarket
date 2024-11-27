// Conversation.java

package com.example.the6ixmarket;

public class Conversation {
    private String conversationId;
    private String lastMessage;
    private long timestamp;
    private String otherUserId;

    public Conversation() {
        // Default constructor required for calls to DataSnapshot.getValue(Conversation.class)
    }

    public Conversation(String conversationId, String lastMessage, long timestamp, String otherUserId) {
        this.conversationId = conversationId;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.otherUserId = otherUserId;
    }

    // Getters and Setters
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }
}
