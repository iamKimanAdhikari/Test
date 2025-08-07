package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryMenuActivity extends AppCompatActivity {
    private Button btnExerciseHistory, btnMealHistory, btnPrevious;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_history_menu);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnExerciseHistory = findViewById(R.id.btnExerciseHistory);
        btnMealHistory = findViewById(R.id.btnMealHistory);
        btnPrevious = findViewById(R.id.btnPrevious);
    }

    private void setupClickListeners() {
        btnExerciseHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryMenuActivity.this, ExerciseHistoryActivity.class);
            startActivity(intent);
        });

        btnMealHistory.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryMenuActivity.this, MealHistoryActivity.class);
            startActivity(intent);
        });

        btnPrevious.setOnClickListener(v -> finish());
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