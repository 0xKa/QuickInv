package com.example.quickinv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickinv.R;
import com.example.quickinv.database.UserDAO;
import com.example.quickinv.models.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;
    private TextView loginLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        registerButton = findViewById(R.id.register_button);
        loginLinkTextView = findViewById(R.id.login_link_text);

        registerButton.setOnClickListener(v -> performRegistration());

        loginLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegistration() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validation
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return;
        }

        if (username.length() < 3) {
            usernameEditText.setError("Username must be at least 3 characters");
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!email.contains("@")) {
            emailEditText.setError("Enter a valid email");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 4) {
            passwordEditText.setError("Password must be at least 4 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return;
        }

        // Check if username exists
        UserDAO userDAO = new UserDAO(this);
        if (userDAO.isUsernameExists(username)) {
            usernameEditText.setError("Username already exists");
            return;
        }

        // Register user
        User newUser = new User(username, password, email);
        long result = userDAO.registerUser(newUser);

        if (result != -1) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
