package com.example.healthtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyConsentActivity extends AppCompatActivity {
    private TextView tvPrivacyText;
    private CheckBox cbDataCollection, cbAnalytics, cbMarketing;
    private Button btnAccept, btnDecline, btnViewPolicy;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_privacy_consent);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        tvPrivacyText = findViewById(R.id.tvPrivacyText);
        cbDataCollection = findViewById(R.id.cbDataCollection);
        cbAnalytics = findViewById(R.id.cbAnalytics);
        cbMarketing = findViewById(R.id.cbMarketing);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);
        btnViewPolicy = findViewById(R.id.btnViewPolicy);

        // Set privacy text
        tvPrivacyText.setText(
                "Welcome to Health Tracker App!\n\n" +
                        "We value your privacy and want to be transparent about how we handle your data. " +
                        "Please review and customize your privacy preferences below:\n\n" +
                        "• Essential Data Collection: Required for core app functionality (health tracking, profile management)\n" +
                        "• Analytics: Helps us improve the app experience\n" +
                        "• Marketing: Personalized recommendations and updates\n\n" +
                        "You can change these preferences at any time in Settings."
        );
    }

    private void setupClickListeners() {
        btnAccept.setOnClickListener(v -> {
            if (!cbDataCollection.isChecked()) {
                Toast.makeText(this, "Essential data collection is required for the app to function",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Save consent preferences
            storageManager.putSecureBoolean("consentGiven", true);
            storageManager.putSecureBoolean("essentialDataConsent", cbDataCollection.isChecked());
            storageManager.putBoolean("analyticsConsent", cbAnalytics.isChecked());
            storageManager.putBoolean("marketingConsent", cbMarketing.isChecked());
            storageManager.putString("consentDate", java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));

            Toast.makeText(this, "Privacy preferences saved", Toast.LENGTH_SHORT).show();
            navigateToRegistration();
        });

        btnDecline.setOnClickListener(v -> {
            Toast.makeText(this, "You must accept essential data collection to use this app",
                    Toast.LENGTH_LONG).show();
            finish();
        });

        btnViewPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(PrivacyConsentActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        // Essential data collection is mandatory
        cbDataCollection.setChecked(true);
        cbDataCollection.setEnabled(false);
    }

    private void navigateToRegistration() {
        Intent intent = new Intent(PrivacyConsentActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void applyTheme() {
        boolean isDarkMode = storageManager.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
    }
}