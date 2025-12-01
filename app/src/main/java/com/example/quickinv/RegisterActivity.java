package com.example.quickinv;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        nameInput = findViewById(R.id.register_name);
        emailInput = findViewById(R.id.register_email);
        passwordInput = findViewById(R.id.register_password);
        confirmInput = findViewById(R.id.register_confirm);

        MaterialButton registerButton = findViewById(R.id.register_submit);
        LinearLayout loginRow = findViewById(R.id.register_login_row);

        registerButton.setOnClickListener(v -> handleRegister());
        loginRow.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String name = getTextOrEmpty(nameInput);
        String email = getTextOrEmpty(emailInput);
        String password = getTextOrEmpty(passwordInput);
        String confirm = getTextOrEmpty(confirmInput);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.login_error_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, R.string.register_error_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, R.string.splash_status_ready, Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getTextOrEmpty(TextInputEditText view) {
        return view.getText() == null ? "" : view.getText().toString().trim();
    }
}

