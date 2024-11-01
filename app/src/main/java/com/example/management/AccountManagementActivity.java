package com.example.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import androidx.appcompat.widget.Toolbar;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountManagementActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone;
    private RadioGroup radioGroupUserType;
    private String apiUrl = "http://yourserver.com/api/users"; // Replace with your actual API base URL
    private String userId = "exampleUserId"; // Replace with actual user ID

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

        // Load existing user information
        loadUserInformation();

        // Set up click listener for the save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }


    private void loadUserInformation() {
        // Load user data from the API
        String url = apiUrl + "/" + userId; // Construct the URL

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountManagementActivity.this, "Error loading user information: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String userType = (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButtonBuyer) ? "buyer" : "seller";

        // Create a JSON object for the updated user data
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

        // Send the updated user data to the API
        String url = apiUrl + "/" + userId; // Construct the URL for update
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, userJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(AccountManagementActivity.this, "User information updated successfully.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AccountManagementActivity.this, "Error updating user information: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}

