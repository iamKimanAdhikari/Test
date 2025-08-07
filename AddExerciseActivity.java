package com.example.healthtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddExerciseActivity extends AppCompatActivity {
    private EditText etExerciseName, etDuration, etCaloriesBurnt;
    private TextView tvHealthInsight, tvRecommendation;
    private Button btnPrevious, btnNext;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_add_exercise);

        initializeViews();
        loadExistingData();
        displayHealthInsights();
        setupClickListeners();
    }

    private void initializeViews() {
        etExerciseName = findViewById(R.id.etExerciseName);
        etDuration = findViewById(R.id.etDuration);
        etCaloriesBurnt = findViewById(R.id.etCaloriesBurnt);
        tvHealthInsight = findViewById(R.id.tvHealthInsight);
        tvRecommendation = findViewById(R.id.tvRecommendation);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void loadExistingData() {
        // Load data from intent if coming from edit
        Intent intent = getIntent();
        if (intent.hasExtra("exerciseName")) {
            etExerciseName.setText(intent.getStringExtra("exerciseName"));
            etDuration.setText(intent.getStringExtra("duration"));
            etCaloriesBurnt.setText(intent.getStringExtra("caloriesBurnt"));
        }
    }

    private void displayHealthInsights() {
        // Get user profile data
        String weight = storageManager.getString("userWeight", "70");
        String height = storageManager.getString("userHeight", "170");
        String age = storageManager.getString("userAge", "25");

        // Calculate BMI and recommendations
        String bmi = InputValidator.calculateBMI(weight, height);
        int recommendedCalories = InputValidator.calculateRecommendedCalories(age, weight, height, "");

        tvHealthInsight.setText("💡 Health Profile:\n" + bmi);
        tvRecommendation.setText(
                "📋 Daily Recommendations:\n" +
                        "🔥 Calories: " + recommendedCalories + " cal\n" +
                        "💧 Water: " + String.format("%.1fL", InputValidator.calculateRecommendedWater(weight)) + "\n" +
                        "🏃 Exercise: 30+ minutes moderate activity"
        );
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String exerciseName = etExerciseName.getText().toString().trim();
            String duration = etDuration.getText().toString().trim();
            String caloriesBurnt = etCaloriesBurnt.getText().toString().trim();

            // Enhanced validation using InputValidator
            InputValidator.ValidationResult validation =
                    InputValidator.validateExercise(exerciseName, duration, caloriesBurnt);

            if (!validation.isValid()) {
                showValidationErrors(validation);
                return;
            }

            // Show exercise summary before proceeding
            showExerciseSummary(exerciseName, duration, caloriesBurnt);
        });
    }

    private void showValidationErrors(InputValidator.ValidationResult validation) {
        new AlertDialog.Builder(this)
                .setTitle("Input Validation Errors")
                .setMessage(validation.getErrorMessage())
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showExerciseSummary(String exerciseName, String duration, String caloriesBurnt) {
        String summaryMessage =
                "Exercise Summary:\n\n" +
                        "🏃 Exercise: " + exerciseName + "\n" +
                        "⏱️ Duration: " + duration + " minutes\n" +
                        "🔥 Calories Burned: " + caloriesBurnt + " cal\n\n" +
                        "Proceed to meal tracking?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Exercise Entry")
                .setMessage(summaryMessage)
                .setPositiveButton("Continue", (dialog, which) -> proceedToMeals(exerciseName, duration, caloriesBurnt))
                .setNegativeButton("Edit", null)
                .show();
    }

    private void proceedToMeals(String exerciseName, String duration, String caloriesBurnt) {
        // Pass data to next activity via Intent
        Intent intent = new Intent(AddExerciseActivity.this, AddMealsActivity.class);
        intent.putExtra("exerciseName", exerciseName);
        intent.putExtra("duration", duration);
        intent.putExtra("caloriesBurnt", caloriesBurnt);

        // Also pass any existing meal data if coming from edit
        Intent currentIntent = getIntent();
        if (currentIntent.hasExtra("mealName")) {
            intent.putExtra("mealName", currentIntent.getStringExtra("mealName"));
            intent.putExtra("caloriesConsumed", currentIntent.getStringExtra("caloriesConsumed"));
            intent.putExtra("waterIntake", currentIntent.getStringExtra("waterIntake"));
        }

        startActivity(intent);
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