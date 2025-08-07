package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseHistoryActivity extends AppCompatActivity {
    private TextView tvExerciseHistory;
    private Button btnPrevious, btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_exercise_history);

        initializeViews();
        displayExerciseHistory();
        setupClickListeners();
    }

    private void initializeViews() {
        tvExerciseHistory = findViewById(R.id.tvExerciseHistory);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void displayExerciseHistory() {
        String exerciseHistory = sharedPreferences.getString("exerciseHistory", "No exercise history available");
        if (exerciseHistory.equals("")) {
            exerciseHistory = "No exercise history available";
        }
        tvExerciseHistory.setText(exerciseHistory);
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseHistoryActivity.this, MainActivity.class);
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