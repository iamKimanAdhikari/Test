package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMealsActivity extends AppCompatActivity {
    private EditText etMealName, etCaloriesConsumed, etWaterIntake;
    private Button btnPrevious, btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_add_meals); // Corrected layout resource

        initializeViews();
        loadExistingData();
        setupClickListeners();
    }

    private void initializeViews() {
        etMealName = findViewById(R.id.etMealName);
        etCaloriesConsumed = findViewById(R.id.etCaloriesConsumed);
        etWaterIntake = findViewById(R.id.etWaterIntake);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void loadExistingData() {
        // Load data from intent
        Intent intent = getIntent();
        if (intent.hasExtra("mealName")) {
            etMealName.setText(intent.getStringExtra("mealName"));
            etCaloriesConsumed.setText(intent.getStringExtra("caloriesConsumed"));
            etWaterIntake.setText(intent.getStringExtra("waterIntake"));
        }
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String mealName = etMealName.getText().toString().trim();
            String caloriesConsumed = etCaloriesConsumed.getText().toString().trim();
            String waterIntake = etWaterIntake.getText().toString().trim();

            if (mealName.isEmpty() || caloriesConsumed.isEmpty() || waterIntake.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass all data to summary activity via Intent
            Intent intent = new Intent(AddMealsActivity.this, ActivitySummaryActivity.class);

            // Exercise data from previous activity
            Intent currentIntent = getIntent();
            intent.putExtra("exerciseName", currentIntent.getStringExtra("exerciseName"));
            intent.putExtra("duration", currentIntent.getStringExtra("duration"));
            intent.putExtra("caloriesBurnt", currentIntent.getStringExtra("caloriesBurnt"));

            // Meal data from current activity
            intent.putExtra("mealName", mealName);
            intent.putExtra("caloriesConsumed", caloriesConsumed);
            intent.putExtra("waterIntake", waterIntake);

            startActivity(intent);
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