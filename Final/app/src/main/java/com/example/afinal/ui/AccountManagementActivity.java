package com.example.afinal.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;
import com.example.afinal.database.UserRepository;
import com.example.afinal.model.User;

public class AccountManagementActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone;
    private RadioGroup radioGroupUserType;
    private UserRepository userRepository;
    private long userId = 1; // Mock user ID; replace with real logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        radioGroupUserType = findViewById(R.id.radioGroupUserType);
        Button buttonSave = findViewById(R.id.buttonSave);
        Button buttonBack = findViewById(R.id.backButton);

        // Initialize repository
        userRepository = new UserRepository(this);

        // Load user data
        loadUserInformation();

        // Set listeners
        buttonSave.setOnClickListener(v -> saveUserInformation());
        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadUserInformation() {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            editTextName.setText(user.getUsername());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhone());

            if ("buyer".equals(user.getUserType())) {
                radioGroupUserType.check(R.id.radioButtonBuyer);
            } else {
                radioGroupUserType.check(R.id.radioButtonSeller);
            }
        } else {
            Toast.makeText(this, "No user data found. Please set up your account.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserInformation() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String userType = (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButtonBuyer) ? "buyer" : "seller";

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and email are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(userId, name, email, phone, userType);
        long result = userRepository.insertUser(user);

        if (result != -1) {
            Toast.makeText(this, "User information saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save user information. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
