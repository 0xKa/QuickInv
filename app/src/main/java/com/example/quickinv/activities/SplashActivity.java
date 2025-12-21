package com.example.quickinv.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickinv.R;
import com.example.quickinv.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Fade in animation
        ImageView logoImageView = findViewById(R.id.logo_image);
        TextView appNameTextView = findViewById(R.id.app_name_text);

        logoImageView.setAlpha(0f);
        appNameTextView.setAlpha(0f);

        logoImageView.animate().alpha(1f).setDuration(1000).start();
        appNameTextView.animate().alpha(1f).setDuration(1000).start();

        // Delayed navigation
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);

            Intent intent;
            if (sessionManager.isLoggedIn()) {
                intent = new Intent(SplashActivity.this, InventoryActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
