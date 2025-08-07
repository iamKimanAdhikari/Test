package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivitySummaryActivity extends AppCompatActivity {
    private TextView tvExerciseInfo, tvMealInfo;
    private Button btnPrevious, btnNext, btnEditExercise, btnEditMeal;
    private SharedPreferences sharedPreferences;

    // Data from intents
    private String exerciseName, duration, caloriesBurnt;
    private String mealName, caloriesConsumed, waterIntake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_summary);

        initializeViews();
        loadDataFromIntent();
        displaySummary();
        setupClickListeners();
    }

    private void initializeViews() {
        tvExerciseInfo = findViewById(R.id.tvExerciseInfo);
        tvMealInfo = findViewById(R.id.tvMealInfo);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnEditExercise = findViewById(R.id.btnEditExercise);
        btnEditMeal = findViewById(R.id.btnEditMeal);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        exerciseName = intent.getStringExtra("exerciseName");
        duration = intent.getStringExtra("duration");
        caloriesBurnt = intent.getStringExtra("caloriesBurnt");
        mealName = intent.getStringExtra("mealName");
        caloriesConsumed = intent.getStringExtra("caloriesConsumed");
        waterIntake = intent.getStringExtra("waterIntake");
    }

    private void displaySummary() {
        String exerciseInfo = "Exercise: " + (exerciseName != null ? exerciseName : "") + "\n" +
                "Duration: " + (duration != null ? duration : "") + " minutes\n" +
                "Calories Burnt: " + (caloriesBurnt != null ? caloriesBurnt : "") + " cal";

        String mealInfo = "Meal: " + (mealName != null ? mealName : "") + "\n" +
                "Calories Consumed: " + (caloriesConsumed != null ? caloriesConsumed : "") + " cal\n" +
                "Water Intake: " + (waterIntake != null ? waterIntake : "") + " liters";

        tvExerciseInfo.setText(exerciseInfo);
        tvMealInfo.setText(mealInfo);
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnEditExercise.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySummaryActivity.this, AddExerciseActivity.class);
            intent.putExtra("exerciseName", exerciseName);
            intent.putExtra("duration", duration);
            intent.putExtra("caloriesBurnt", caloriesBurnt);
            intent.putExtra("mealName", mealName);
            intent.putExtra("caloriesConsumed", caloriesConsumed);
            intent.putExtra("waterIntake", waterIntake);
            startActivity(intent);
            finish();
        });

        btnEditMeal.setOnClickListener(v -> {
            Intent intent = new Intent(ActivitySummaryActivity.this, AddMealsActivity.class);
            intent.putExtra("exerciseName", exerciseName);
            intent.putExtra("duration", duration);
            intent.putExtra("caloriesBurnt", caloriesBurnt);
            intent.putExtra("mealName", mealName);
            intent.putExtra("caloriesConsumed", caloriesConsumed);
            intent.putExtra("waterIntake", waterIntake);
            startActivity(intent);
            finish();
        });

        btnNext.setOnClickListener(v -> {
            saveDataToHistory();
            Intent intent = new Intent(ActivitySummaryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void saveDataToHistory() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save to exercise history
        String exerciseHistory = sharedPreferences.getString("exerciseHistory", "");
        String newExerciseEntry = timestamp + " | " + exerciseName + " | " + duration + " min | " + caloriesBurnt + " cal\n";
        exerciseHistory += newExerciseEntry;

        // Save to meal history
        String mealHistory = sharedPreferences.getString("mealHistory", "");
        String newMealEntry = timestamp + " | " + mealName + " | " + caloriesConsumed + " cal | " + waterIntake + " L\n";
        mealHistory += newMealEntry;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("exerciseHistory", exerciseHistory);
        editor.putString("mealHistory", mealHistory);
        editor.apply();
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