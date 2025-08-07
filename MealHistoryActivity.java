package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MealHistoryActivity extends AppCompatActivity {
    private TextView tvMealHistory;
    private Button btnPrevious, btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_meal_history);

        initializeViews();
        displayMealHistory();
        setupClickListeners();
    }

    private void initializeViews() {
        tvMealHistory = findViewById(R.id.tvMealHistory);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void displayMealHistory() {
        String mealHistory = sharedPreferences.getString("mealHistory", "No meal history available");
        if (mealHistory.equals("")) {
            mealHistory = "No meal history available";
        }
        tvMealHistory.setText(mealHistory);
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(MealHistoryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void applyTheme() {
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }
    }
}