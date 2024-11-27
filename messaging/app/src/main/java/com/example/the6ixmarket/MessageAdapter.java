// MessageAdapter.java

package com.example.the6ixmarket;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private String currentUserId;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> messages, String currentUserId) {
        super(context, 0, messages);
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);

        if (message == null) return convertView;

        if (message.getSenderId().equals(currentUserId)) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_received, parent, false);
        }

        TextView messageText = convertView.findViewById(R.id.message_text);
        messageText.setText(message.getText());

        return convertView;
    }
}
