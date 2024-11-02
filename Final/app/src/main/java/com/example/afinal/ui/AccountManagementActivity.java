package com.example.afinal.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afinal.R;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountManagementActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone;
    private RadioGroup radioGroupUserType;
    private String apiUrl = "http://yourserver.com/api/users";
    private String userId = "exampleUserId";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        radioGroupUserType = findViewById(R.id.radioGroupUserType);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonBack = findViewById(R.id.backButton);

        // Set up back button listener
        buttonBack.setOnClickListener(v -> finish());

        // Load existing user information
        loadUserInformation();

        // Set up click listener for the save button
        buttonSave.setOnClickListener(v -> saveUserInformation());
    }

    private void loadUserInformation() {
        String url = apiUrl + "/" + userId; // Construct the URL
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Populate fields with existing user data
                        editTextName.setText(response.getString("name"));
                        editTextEmail.setText(response.getString("email"));
                        editTextPhone.setText(response.getString("phone"));
                        String userType = response.getString("userType");
                        if ("buyer".equals(userType)) {
                            radioGroupUserType.check(R.id.radioButtonBuyer);
                        } else {
                            radioGroupUserType.check(R.id.radioButtonSeller);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(AccountManagementActivity.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(AccountManagementActivity.this, "Error loading user information: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String userType = (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButtonBuyer) ? "buyer" : "seller";

        JSONObject userJson = new JSONObject();
        try {
            userJson.put("name", name);
            userJson.put("email", email);
            userJson.put("phone", phone);
            userJson.put("userType", userType);
        } catch (JSONException e) {
            Toast.makeText(this, "Error creating JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        String url = apiUrl + "/" + userId; // Construct the URL for update
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, userJson,
                response -> Toast.makeText(AccountManagementActivity.this, "User information updated successfully.", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(AccountManagementActivity.this, "Error updating user information: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonObjectRequest);
    }
}
