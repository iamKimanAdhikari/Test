package com.example.healthtrackerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    private EditText etName, etAge, etWeight, etHeight;
    private Button btnPrevious, btnNext;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("HealthTrackerPrefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_edit_profile);

        initializeViews();
        loadCurrentProfile();
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
    }

    private void loadCurrentProfile() {
        String name = sharedPreferences.getString("userName", "");
        String age = sharedPreferences.getString("userAge", "");
        String weight = sharedPreferences.getString("userWeight", "");
        String height = sharedPreferences.getString("userHeight", "");

        etName.setText(name);
        etAge.setText(age);
        etWeight.setText(weight);
        etHeight.setText(height);
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());

        btnNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();
            String height = etHeight.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || weight.isEmpty() || height.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save updated profile
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userName", name);
            editor.putString("userAge", age);
            editor.putString("userWeight", weight);
            editor.putString("userHeight", height);
            editor.apply();

            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to Exercise page instead of MainActivity
            Intent intent = new Intent(EditProfileActivity.this, AddExerciseActivity.class);
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