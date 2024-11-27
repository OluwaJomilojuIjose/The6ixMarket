package com.example.the6ixmarket;

import java.time.Instant;

public class ChatMessage {
    private String sender;
    private String message;
    private Instant timestamp;

    public ChatMessage() {

    }

    public ChatMessage(String sender, String message, Instant timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
