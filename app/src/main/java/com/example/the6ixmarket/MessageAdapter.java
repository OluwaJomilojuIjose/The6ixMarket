package com.example.the6ixmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.widget.ArrayAdapter;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        // Get the current message
        Message message = getItem(position);

        // Set the message content
        TextView messageText = convertView.findViewById(R.id.message_text);
        messageText.setText(message.getContent());

        // Adjust background based on sender
        if (message.isSentByUser()) {
            messageText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_message_sent));
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            messageText.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_message_received));
            messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        return convertView;
    }
}
