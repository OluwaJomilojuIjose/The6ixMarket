package com.example.afinal.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.afinal.R;
import com.example.afinal.database.UserRepository;
import com.example.afinal.model.User;

public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    private EditText editTextUsername, editTextEmail, editTextPhone;
    private RadioGroup radioGroupUserType;
    private UserRepository userRepository;
    private long userId = 1;

    public AccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        radioGroupUserType = view.findViewById(R.id.radioGroupUserType);
        Button saveButton = view.findViewById(R.id.buttonSave);

        try {
            userRepository = new UserRepository(getContext());
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize UserRepository", e);
            Toast.makeText(getContext(), "Failed to initialize the database. Please restart the app.", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Load existing user information
        loadUserInformation();


        saveButton.setOnClickListener(v -> saveUserInformation());

        return view;
    }

    private void loadUserInformation() {
        User user = userRepository.getUserById(userId);
        if (user != null) {
            editTextUsername.setText(user.getUsername());
            editTextEmail.setText(user.getEmail());
            editTextPhone.setText(user.getPhone());

            if ("buyer".equals(user.getUserType())) {
                radioGroupUserType.check(R.id.radioButtonBuyer);
            } else {
                radioGroupUserType.check(R.id.radioButtonSeller);
            }
        }
    }

    private void saveUserInformation() {
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String userType = (radioGroupUserType.getCheckedRadioButtonId() == R.id.radioButtonBuyer) ? "buyer" : "seller";

        long result = userRepository.insertUser(username, email, phone, userType);
        if (result != -1) {
            Toast.makeText(getContext(), "User information saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to save user information. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
