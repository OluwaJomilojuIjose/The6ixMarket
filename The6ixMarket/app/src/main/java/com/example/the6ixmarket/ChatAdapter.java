package com.example.the6ixmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_message, parent, false);
        }

        ChatMessage message = getItem(position);
        TextView senderText = convertView.findViewById(R.id.senderText);
        TextView messageText = convertView.findViewById(R.id.messageText);

        senderText.setText(message.getSender());
        messageText.setText(message.getMessage());

        return convertView;
    }
}
