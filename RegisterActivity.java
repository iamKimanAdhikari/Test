package com.example.healthtrackerapp;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etAge, etWeight, etHeight, etEmail, etPassword;
    private Button btnRegister, btnBackToLogin;
    private ImageButton btnTogglePassword;
    private SecureStorageManager storageManager;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_register);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> performRegistration());
        btnBackToLogin.setOnClickListener(v -> finish());

        if (btnTogglePassword != null) {
            btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        }
    }

    private void performRegistration() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic check for empty fields
        if (name.isEmpty() || age.isEmpty() || weight.isEmpty() ||
                height.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enhanced validation using InputValidator
        InputValidator.ValidationResult profileValidation =
                InputValidator.validateProfile(name, age, weight, height);

        if (!profileValidation.isValid()) {
            showValidationErrors("Profile Validation Errors", profileValidation);
            return;
        }

        InputValidator.ValidationResult emailValidation = InputValidator.validateEmail(email);
        if (!emailValidation.isValid()) {
            showValidationErrors("Email Validation Error", emailValidation);
            return;
        }

        InputValidator.ValidationResult passwordValidation = InputValidator.validatePassword(password);
        if (!passwordValidation.isValid()) {
            showValidationErrors("Password Validation Error", passwordValidation);
            return;
        }

        // Check if user already exists
        String existingEmail = storageManager.getSecureString("userEmail", "");
        if (!existingEmail.isEmpty() && existingEmail.equals(email)) {
            Toast.makeText(this, "An account with this email already exists", Toast.LENGTH_LONG).show();
            return;
        }

        // Show registration summary before saving
        showRegistrationSummary(name, age, weight, height, email, password);
    }

    private void showValidationErrors(String title, InputValidator.ValidationResult validation) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(validation.getErrorMessage())
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showRegistrationSummary(String name, String age, String weight,
                                         String height, String email, String password) {
        // Calculate BMI for display
        String bmiInfo = InputValidator.calculateBMI(weight, height);
        int recommendedCalories = InputValidator.calculateRecommendedCalories(age, weight, height, "");
        float recommendedWater = InputValidator.calculateRecommendedWater(weight);

        String summaryMessage =
                "Registration Summary:\n\n" +
                        "👤 Name: " + name + "\n" +
                        "🎂 Age: " + age + " years\n" +
                        "⚖️ Weight: " + weight + " kg\n" +
                        "📏 Height: " + height + " cm\n" +
                        "📧 Email: " + email + "\n\n" +
                        "📊 Health Profile:\n" +
                        bmiInfo + "\n" +
                        "🔥 Recommended Daily Calories: " + recommendedCalories + "\n" +
                        "💧 Recommended Water Intake: " + String.format("%.1fL", recommendedWater) + "\n\n" +
                        "Proceed with registration?";

        new AlertDialog.Builder(this)
                .setTitle("Confirm Registration")
                .setMessage(summaryMessage)
                .setPositiveButton("Register", (dialog, which) -> saveRegistration(name, age, weight, height, email, password))
                .setNegativeButton("Edit", null)
                .show();
    }

    private void saveRegistration(String name, String age, String weight,
                                  String height, String email, String password) {
        try {
            // Save sensitive data encrypted, non-sensitive data unencrypted
            storageManager.putSecureString("userEmail", email);
            storageManager.putSecureString("userPassword", password);
            storageManager.putSecureString("userName", name);
            storageManager.putString("userAge", age);
            storageManager.putString("userWeight", weight);
            storageManager.putString("userHeight", height);

            // Save registration date
            storageManager.putString("registrationDate",
                    java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));

            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (btnTogglePassword != null) {
                btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            if (btnTogglePassword != null) {
                btnTogglePassword.setImageResource(R.drawable.ic_eye_off);
            }
        }
        etPassword.setSelection(etPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void applyTheme() {
        try {
            boolean isDarkMode = storageManager.getBoolean("isDarkMode", false);
            if (isDarkMode) {
                setTheme(R.style.DarkTheme);
            } else {
                setTheme(R.style.LightTheme);
            }
        } catch (Exception e) {
            // Use default theme if storage fails
            setTheme(android.R.style.Theme_Material_Light);
        }
    }
}