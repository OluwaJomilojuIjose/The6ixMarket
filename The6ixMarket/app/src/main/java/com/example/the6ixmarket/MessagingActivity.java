package com.example.the6ixmarket;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    List<ChatMessage> chatMessages;
    ChatAdapter chatAdapter;

    ListView listViewMessages;
    EditText inputMessage;
    Button sendButton;

    String chatRoomId;
    String serverUrl = "http://10.0.2.2:3000"; // Use this IP for Android Emulator
//    String serverUrl = "http://192.168.x.x:3000"; // Use this for Physical android device

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Get data from intent
        String itemName = getIntent().getStringExtra("ITEM_TITLE");
        String sellerName = getIntent().getStringExtra("SELLER_NAME");
        chatRoomId = itemName + "_" + sellerName + "_" + user.getDisplayName();

        // Initialize UI components
        listViewMessages = findViewById(R.id.listViewMessages);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);

        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        listViewMessages.setAdapter(chatAdapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Load chat messages
        loadMessages();

        // Send message
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(MessagingActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(message);
                inputMessage.setText("");
            }
        });
    }

    private void loadMessages() {
        String url = serverUrl + "/messages/" + chatRoomId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    chatMessages.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            ChatMessage message = new ChatMessage(
                                    obj.getString("sender"),
                                    obj.getString("message"),
                                    Instant.parse(obj.getString("timestamp"))
                            );
                            chatMessages.add(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    chatAdapter.notifyDataSetChanged();
                    listViewMessages.setSelection(chatMessages.size() - 1);
                },
                error -> {
                    Toast.makeText(MessagingActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                    Log.e("DATABASE ERROR", error.getMessage());
                });

        requestQueue.add(request);
    }

    private void sendMessage(String message) {
        String url = serverUrl + "/messages";
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("chatRoomId", chatRoomId);
            requestBody.put("sender", user.getEmail());
            requestBody.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    loadMessages(); // Refresh messages after sending
                },
                error -> Toast.makeText(MessagingActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show());

        requestQueue.add(request);
    }
}
