package com.example.quickinv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickinv.R;
import com.example.quickinv.database.DatabaseHelper;
import com.example.quickinv.database.UserDAO;
import com.example.quickinv.models.User;
import com.example.quickinv.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerLinkTextView = findViewById(R.id.register_link_text);

        loginButton.setOnClickListener(v -> performLogin());

        registerLinkTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validation
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
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

        // Database login
        UserDAO userDAO = new UserDAO(this);
        User user = userDAO.loginUser(username, password);

        if (user != null) {
            // Save session
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.createLoginSession(user.getId(), user.getUsername());

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Navigate to inventory
            Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
