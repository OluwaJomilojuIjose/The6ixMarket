// MessagesActivity.java

package com.example.the6ixmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    private static final String TAG = "MessagesActivity";

    private ListView conversationsListView;
    private ConversationsAdapter conversationsAdapter;
    private ArrayList<Conversation> conversationList;

    private FirebaseAuth mAuth;
    private DatabaseReference userConversationsRef;
    private DatabaseReference messagesRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        conversationsListView = findViewById(R.id.conversations_list_view);
        conversationList = new ArrayList<>();
        conversationsAdapter = new ConversationsAdapter(this, conversationList);
        conversationsListView.setAdapter(conversationsAdapter);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userConversationsRef = FirebaseDatabase.getInstance().getReference("user_conversations").child(currentUserId);
        messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        loadConversations();

        conversationsListView.setOnItemClickListener((parent, view, position, id) -> {
            Conversation conversation = conversationList.get(position);
            Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
            intent.putExtra("CONVERSATION_ID", conversation.getConversationId());
            intent.putExtra("OTHER_USER_ID", conversation.getOtherUserId());
            startActivity(intent);
        });
    }

    private void loadConversations() {
        Log.d(TAG, "Loading conversations for user: " + currentUserId);

        userConversationsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                String conversationId = dataSnapshot.getKey();
                Log.d(TAG, "Conversation added: " + conversationId);
                loadConversationDetails(conversationId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                String conversationId = dataSnapshot.getKey();
                Log.d(TAG, "Conversation changed: " + conversationId);
                loadConversationDetails(conversationId);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String conversationId = dataSnapshot.getKey();
                Log.d(TAG, "Conversation removed: " + conversationId);
                removeConversation(conversationId);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Not needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessagesActivity.this, "Failed to load conversations.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to load conversations.", error.toException());
            }
        });
    }

    private void loadConversationDetails(String conversationId) {
        DatabaseReference conversationRef = messagesRef.child(conversationId);
        conversationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lastMessage = snapshot.child("lastMessage").getValue(String.class);
                Long timestamp = snapshot.child("timestamp").getValue(Long.class);
                String userId1 = snapshot.child("user1").getValue(String.class);
                String userId2 = snapshot.child("user2").getValue(String.class);

                if (userId1 == null || userId2 == null) {
                    Log.e(TAG, "User IDs are null for conversation: " + conversationId);
                    return;
                }

                String otherUserId = currentUserId.equals(userId1) ? userId2 : userId1;

                Conversation conversation = new Conversation(conversationId, lastMessage != null ? lastMessage : "", timestamp != null ? timestamp : 0, otherUserId);

                // Remove existing conversation if any
                for (int i = 0; i < conversationList.size(); i++) {
                    if (conversationList.get(i).getConversationId().equals(conversationId)) {
                        conversationList.remove(i);
                        break;
                    }
                }

                conversationList.add(conversation);
                conversationsAdapter.notifyDataSetChanged();
                Log.d(TAG, "Conversation loaded: " + conversationId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to load conversation details.", error.toException());
            }
        });
    }

    private void removeConversation(String conversationId) {
        for (int i = 0; i < conversationList.size(); i++) {
            if (conversationList.get(i).getConversationId().equals(conversationId)) {
                conversationList.remove(i);
                conversationsAdapter.notifyDataSetChanged();
                Log.d(TAG, "Conversation removed from list: " + conversationId);
                break;
            }
        }
    }
}
