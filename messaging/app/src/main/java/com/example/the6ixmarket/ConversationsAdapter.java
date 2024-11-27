// ConversationsAdapter.java

package com.example.the6ixmarket;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationsAdapter extends ArrayAdapter<Conversation> {

    private static final String TAG = "ConversationsAdapter";

    public ConversationsAdapter(@NonNull Context context, @NonNull List<Conversation> conversations) {
        super(context, 0, conversations);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation conversation = getItem(position);

        if (conversation == null) {
            Log.e(TAG, "Conversation is null at position: " + position);
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
        }

        TextView otherUserNameTextView = convertView.findViewById(R.id.other_user_name);
        TextView lastMessageTextView = convertView.findViewById(R.id.last_message);
        TextView timestampTextView = convertView.findViewById(R.id.timestamp);

        // Fetch and display the other user's name
        String otherUserId = conversation.getOtherUserId();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(otherUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String otherUserName = snapshot.child("name").getValue(String.class);
                if (otherUserName != null) {
                    otherUserNameTextView.setText(otherUserName);
                } else {
                    otherUserNameTextView.setText("Unknown User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                otherUserNameTextView.setText("Unknown User");
                Log.e(TAG, "Failed to load other user name.", error.toException());
            }
        });

        lastMessageTextView.setText(conversation.getLastMessage());
        timestampTextView.setText(formatTimestamp(conversation.getTimestamp()));

        return convertView;
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
