package com.example.the6ixmarket;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {

    private EditText messageInput;
    private Button sendButton;
    private ListView messageListView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Initialize UI components
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        messageListView = findViewById(R.id.message_list_view);

        // Initialize the message list
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        messageListView.setAdapter(messageAdapter);

        // Handle send button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();

        if (TextUtils.isEmpty(messageText)) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the message to the list
        Message newMessage = new Message(messageText, true); // Sent by the user
        messageList.add(newMessage);

        // Clear the input field
        messageInput.setText("");

        // Notify the adapter to update the ListView
        messageAdapter.notifyDataSetChanged();

        // Simulate a response from the other user
        simulateResponse();
    }

    private void simulateResponse() {
        // Simulate a delay for the response
        messageListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message response = new Message("This is an automated response!", false); // Received message
                messageList.add(response);
                messageAdapter.notifyDataSetChanged();
            }
        }, 1000); // 1-second delay
    }
}
