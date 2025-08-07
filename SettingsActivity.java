package com.example.healthtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchDarkMode, switchNotifications;
    private Button btnPrevious, btnPrivacySettings, btnHealthTrends, btnAbout;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_settings);

        initializeViews();
        loadCurrentSettings();
        setupClickListeners();
    }

    private void initializeViews() {
        switchDarkMode = findViewById(R.id.switchDarkMode);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrivacySettings = findViewById(R.id.btnPrivacySettings);
        btnHealthTrends = findViewById(R.id.btnHealthTrends);
        btnAbout = findViewById(R.id.btnAbout);
    }

    private void loadCurrentSettings() {
        boolean isDarkMode = storageManager.getBoolean("isDarkMode", false);
        boolean notificationsEnabled = storageManager.getBoolean("notificationsEnabled", true);

        switchDarkMode.setChecked(isDarkMode);
        switchNotifications.setChecked(notificationsEnabled);
    }

    private void setupClickListeners() {
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storageManager.putBoolean("isDarkMode", isChecked);
            storageManager.putBoolean("themeChanged", true);
            Toast.makeText(this, "Theme will be applied when you navigate back", Toast.LENGTH_SHORT).show();
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storageManager.putBoolean("notificationsEnabled", isChecked);
            String message = isChecked ? "Notifications enabled" : "Notifications disabled";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        btnPrevious.setOnClickListener(v -> finish());

        // NEW: Privacy Settings Integration
        btnPrivacySettings.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, PrivacySettingsActivity.class);
            startActivity(intent);
        });

        // NEW: Direct access to Health Trends
        btnHealthTrends.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HealthTrendsActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> showAboutDialog());
    }

    private void showAboutDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("About Health Tracker")
                .setMessage("Health Tracker App v1.0\n\n" +
                        "A comprehensive personal health tracking application that helps you monitor:\n" +
                        "• Exercise routines and calories burned\n" +
                        "• Diet plans and calorie consumption\n" +
                        "• Water intake and hydration\n" +
                        "• Health trends and progress\n\n" +
                        "Built with privacy-first principles in compliance with GDPR and Australian Privacy Principles (APPs).\n\n" +
                        "Your health data is encrypted and stored securely on your device.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void applyTheme() {
        boolean isDarkMode = storageManager.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (storageManager.getBoolean("themeChanged", false)) {
            // The theme change will be handled by the calling activity
        }
    }
}