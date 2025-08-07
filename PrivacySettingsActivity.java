package com.example.healthtrackerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacySettingsActivity extends AppCompatActivity {
    private TextView tvConsentDate;
    private Switch switchAnalytics, switchMarketing;
    private Button btnBack, btnExportData, btnDeleteAllData, btnViewPolicy;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_privacy_settings);

        initializeViews();
        loadCurrentSettings();
        setupClickListeners();
    }

    private void initializeViews() {
        tvConsentDate = findViewById(R.id.tvConsentDate);
        switchAnalytics = findViewById(R.id.switchAnalytics);
        switchMarketing = findViewById(R.id.switchMarketing);
        btnBack = findViewById(R.id.btnBack);
        btnExportData = findViewById(R.id.btnExportData);
        btnDeleteAllData = findViewById(R.id.btnDeleteAllData);
        btnViewPolicy = findViewById(R.id.btnViewPolicy);
    }

    private void loadCurrentSettings() {
        String consentDate = storageManager.getString("consentDate", "Not available");
        tvConsentDate.setText("Consent given on: " + consentDate);

        switchAnalytics.setChecked(storageManager.getBoolean("analyticsConsent", false));
        switchMarketing.setChecked(storageManager.getBoolean("marketingConsent", false));
    }

    private void setupClickListeners() {
        switchAnalytics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storageManager.putBoolean("analyticsConsent", isChecked);
            Toast.makeText(this, "Analytics preference updated", Toast.LENGTH_SHORT).show();
        });

        switchMarketing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            storageManager.putBoolean("marketingConsent", isChecked);
            Toast.makeText(this, "Marketing preference updated", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> finish());

        btnViewPolicy.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    PrivacySettingsActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        });

        btnExportData.setOnClickListener(v -> exportUserData());

        btnDeleteAllData.setOnClickListener(v -> showDeleteDataDialog());
    }

    private void exportUserData() {
        // Create a simple data export (in a real app, this would be more comprehensive)
        StringBuilder dataExport = new StringBuilder();
        dataExport.append("HEALTH TRACKER DATA EXPORT\n");
        dataExport.append("Export Date: ").append(java.text.DateFormat.getDateTimeInstance().format(new java.util.Date())).append("\n\n");

        dataExport.append("PROFILE DATA:\n");
        dataExport.append("Name: ").append(storageManager.getSecureString("userName", "N/A")).append("\n");
        dataExport.append("Age: ").append(storageManager.getString("userAge", "N/A")).append("\n");
        dataExport.append("Weight: ").append(storageManager.getString("userWeight", "N/A")).append("\n");
        dataExport.append("Height: ").append(storageManager.getString("userHeight", "N/A")).append("\n");
        dataExport.append("Email: ").append(storageManager.getSecureString("userEmail", "N/A")).append("\n\n");

        dataExport.append("EXERCISE HISTORY:\n");
        dataExport.append(storageManager.getString("exerciseHistory", "No data")).append("\n");

        dataExport.append("MEAL HISTORY:\n");
        dataExport.append(storageManager.getString("mealHistory", "No data")).append("\n");

        dataExport.append("PRIVACY SETTINGS:\n");
        dataExport.append("Analytics Consent: ").append(storageManager.getBoolean("analyticsConsent", false)).append("\n");
        dataExport.append("Marketing Consent: ").append(storageManager.getBoolean("marketingConsent", false)).append("\n");
        dataExport.append("Consent Date: ").append(storageManager.getString("consentDate", "N/A")).append("\n");

        // In a real app, you would save this to a file or send via email
        // For demo purposes, we'll just show it in a dialog
        new AlertDialog.Builder(this)
                .setTitle("Data Export")
                .setMessage("Data export created successfully. In a full implementation, this would be saved as a file or emailed to you.")
                .setPositiveButton("OK", null)
                .show();

        Toast.makeText(this, "Data exported successfully", Toast.LENGTH_SHORT).show();
    }

    private void showDeleteDataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Data")
                .setMessage("This will permanently delete all your health data, profile information, and settings. This action cannot be undone.\n\nAre you sure you want to continue?")
                .setPositiveButton("DELETE ALL DATA", (dialog, which) -> deleteAllUserData())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAllUserData() {
        storageManager.clearAllData();
        Toast.makeText(this, "All data has been deleted", Toast.LENGTH_LONG).show();

        // Navigate back to login screen
        android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
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