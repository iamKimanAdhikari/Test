package com.example.healthtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnHealthEntry, btnViewHistory, btnEditProfile, btnViewTrends, btnSettings, btnLogout;
    private TextView tvWelcome, tvQuickStats;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storageManager = new SecureStorageManager(this);

        // Check if user has given consent
        if (!storageManager.getSecureBoolean("consentGiven", false)) {
            Intent intent = new Intent(MainActivity.this, PrivacyConsentActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        displayWelcomeMessage();
        displayQuickStats();
        setupClickListeners();
    }

    private void initializeViews() {
        btnHealthEntry = findViewById(R.id.btnHealthEntry);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnViewTrends = findViewById(R.id.btnViewTrends); // New button for visual insights
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvQuickStats = findViewById(R.id.tvQuickStats);
    }

    private void displayWelcomeMessage() {
        String userName = storageManager.getSecureString("userName", "User");
        tvWelcome.setText("Welcome back, " + userName + "!");
    }

    private void displayQuickStats() {
        // Calculate quick stats from recent data
        String exerciseHistory = storageManager.getString("exerciseHistory", "");
        String mealHistory = storageManager.getString("mealHistory", "");

        int totalExerciseSessions = 0;
        int totalCaloriesBurned = 0;
        int totalCaloriesConsumed = 0;
        double totalWaterIntake = 0.0;

        // Parse exercise history
        if (!exerciseHistory.isEmpty()) {
            String[] exerciseEntries = exerciseHistory.split("\n");
            for (String entry : exerciseEntries) {
                if (!entry.trim().isEmpty()) {
                    totalExerciseSessions++;
                    String[] parts = entry.split(" \\| ");
                    if (parts.length >= 4) {
                        try {
                            totalCaloriesBurned += Integer.parseInt(parts[3].replaceAll("[^0-9]", ""));
                        } catch (NumberFormatException e) {
                            // Skip invalid entries
                        }
                    }
                }
            }
        }

        // Parse meal history
        if (!mealHistory.isEmpty()) {
            String[] mealEntries = mealHistory.split("\n");
            for (String entry : mealEntries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(" \\| ");
                    if (parts.length >= 4) {
                        try {
                            totalCaloriesConsumed += Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
                            totalWaterIntake += Double.parseDouble(parts[3].replaceAll("[^0-9.]", ""));
                        } catch (NumberFormatException e) {
                            // Skip invalid entries
                        }
                    }
                }
            }
        }

        String statsText = String.format(
                "📊 Your Progress Summary:\n" +
                        "🏃 Exercise Sessions: %d\n" +
                        "🔥 Calories Burned: %d\n" +
                        "🍽️ Calories Consumed: %d\n" +
                        "💧 Water Intake: %.1fL\n" +
                        "⚖️ Net Calories: %d",
                totalExerciseSessions,
                totalCaloriesBurned,
                totalCaloriesConsumed,
                totalWaterIntake,
                (totalCaloriesConsumed - totalCaloriesBurned)
        );

        tvQuickStats.setText(statsText);
    }

    private void setupClickListeners() {
        btnHealthEntry.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
            startActivity(intent);
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryMenuActivity.class);
            startActivity(intent);
        });

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // NEW: Visual Insights Integration
        btnViewTrends.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HealthTrendsActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            storageManager.putSecureBoolean("isLoggedIn", false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
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
    protected void onResume() {
        super.onResume();
        if (storageManager.getBoolean("themeChanged", false)) {
            storageManager.putBoolean("themeChanged", false);
            recreate();
        }
        // Refresh quick stats when returning to main activity
        displayQuickStats();
    }
}