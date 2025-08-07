package com.example.healthtrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private ImageButton btnTogglePassword, btnSettings;
    private SecureStorageManager storageManager;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_login);

        // Check if user is already logged in
        if (storageManager.getSecureBoolean("isLoggedIn", false)) {
            navigateToMain();
            return;
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        try {
            etEmail = findViewById(R.id.etEmail);
            etPassword = findViewById(R.id.etPassword);
            btnLogin = findViewById(R.id.btnLogin);
            btnRegister = findViewById(R.id.btnRegister);
            btnTogglePassword = findViewById(R.id.btnTogglePassword);
            btnSettings = findViewById(R.id.btnSettings);

            // Check if any required views are null
            if (etEmail == null || etPassword == null || btnLogin == null || btnRegister == null) {
                throw new RuntimeException("Required views not found in layout");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Layout error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Only set up password toggle if the button exists
        if (btnTogglePassword != null) {
            btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        }

        // Only set up settings button if it exists
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enhanced validation using InputValidator
        InputValidator.ValidationResult emailValidation = InputValidator.validateEmail(email);
        if (!emailValidation.isValid()) {
            showValidationError("Email Validation Error", emailValidation.getErrorMessage());
            return;
        }

        InputValidator.ValidationResult passwordValidation = InputValidator.validatePassword(password);
        if (!passwordValidation.isValid()) {
            showValidationError("Password Validation Error", passwordValidation.getErrorMessage());
            return;
        }

        try {
            // Validate against encrypted stored credentials
            String savedEmail = storageManager.getSecureString("userEmail", "");
            String savedPassword = storageManager.getSecureString("userPassword", "");

            if (savedEmail.isEmpty()) {
                Toast.makeText(this, "No registered account found. Please register first.", Toast.LENGTH_LONG).show();
                return;
            }

            if (email.equals(savedEmail) && password.equals(savedPassword)) {
                storageManager.putSecureBoolean("isLoggedIn", true);
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showValidationError(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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

    private void navigateToMain() {
        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Navigation error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (storageManager != null && storageManager.getBoolean("themeChanged", false)) {
                storageManager.putBoolean("themeChanged", false);
                recreate();
            }
        } catch (Exception e) {
            // Ignore theme change errors
        }
    }
}