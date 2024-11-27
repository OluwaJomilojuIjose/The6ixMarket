// ChatActivity.java

package com.example.the6ixmarket;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private EditText messageInput;
    private Button sendButton;
    private ListView messageListView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;

    private DatabaseReference messagesRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String otherUserId;
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        messageListView = findViewById(R.id.message_list_view);

        messageList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        conversationId = getIntent().getStringExtra("CONVERSATION_ID");
        otherUserId = getIntent().getStringExtra("OTHER_USER_ID");

        if (conversationId == null || otherUserId == null) {
            Toast.makeText(this, "Conversation data missing.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d(TAG, "Conversation ID: " + conversationId + ", Other User ID: " + otherUserId);

        messageAdapter = new MessageAdapter(this, messageList, currentUserId);
        messageListView.setAdapter(messageAdapter);

        messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(conversationId).child("messages");

        loadMessages();

        sendButton.setOnClickListener(v -> sendMessage());

        // Set the title to the other user's name
        setChatTitle();
    }

    private void setChatTitle() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(otherUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherUserName = snapshot.child("name").getValue(String.class);
                if (otherUserName != null) {
                    setTitle(otherUserName);
                } else {
                    setTitle("Chat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setTitle("Chat");
                Log.e(TAG, "Failed to set chat title.", error.toException());
            }
        });
    }

    private void loadMessages() {
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message != null) {
                        messageList.add(message);
                    }
                }
                messageAdapter.notifyDataSetChanged();
                messageListView.setSelection(messageList.size() - 1);
                Log.d(TAG, "Loaded messages: " + messageList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load messages.", error.toException());
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Sending message: " + messageText);

        DatabaseReference newMessageRef = messagesRef.push();
        String messageId = newMessageRef.getKey();
        Message newMessage = new Message(currentUserId, otherUserId, messageText, System.currentTimeMillis());

        if (messageId != null) {
            newMessageRef.setValue(newMessage).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update last message and timestamp
                    DatabaseReference conversationRef = FirebaseDatabase.getInstance().getReference("messages").child(conversationId);
                    conversationRef.child("lastMessage").setValue(messageText);
                    conversationRef.child("timestamp").setValue(System.currentTimeMillis());
                    conversationRef.child("user1").setValue(currentUserId);
                    conversationRef.child("user2").setValue(otherUserId);

                    // Ensure the conversation is listed under both users
                    DatabaseReference userConversationsRef = FirebaseDatabase.getInstance().getReference("user_conversations");
                    userConversationsRef.child(currentUserId).child(conversationId).setValue(true);
                    userConversationsRef.child(otherUserId).child(conversationId).setValue(true);

                    messageInput.setText("");
                    Log.d(TAG, "Message sent successfully.");
                } else {
                    Log.e(TAG, "Failed to send message.", task.getException());
                    Toast.makeText(ChatActivity.this, "Failed to send message.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Message ID is null. Message not sent.");
            Toast.makeText(this, "Failed to send message.", Toast.LENGTH_SHORT).show();
        }
    }
}
