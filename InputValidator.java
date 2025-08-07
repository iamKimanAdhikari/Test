package com.example.healthtrackerapp;

import android.util.Patterns;
import java.util.ArrayList;
import java.util.List;

public class InputValidator {

    public static class ValidationResult {
        private boolean isValid;
        private List<String> errors;

        public ValidationResult() {
            this.isValid = true;
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            this.errors.add(error);
            this.isValid = false;
        }

        public boolean isValid() {
            return isValid;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getErrorMessage() {
            if (errors.isEmpty()) return "";
            return String.join("\n", errors);
        }
    }

    // Profile validation
    public static ValidationResult validateProfile(String name, String age, String weight, String height) {
        ValidationResult result = new ValidationResult();

        // Name validation
        if (name == null || name.trim().isEmpty()) {
            result.addError("Name is required");
        } else if (name.trim().length() < 2) {
            result.addError("Name must be at least 2 characters");
        } else if (name.trim().length() > 50) {
            result.addError("Name must be less than 50 characters");
        } else if (!name.matches("^[a-zA-Z\\s]+$")) {
            result.addError("Name can only contain letters and spaces");
        }

        // Age validation
        if (age == null || age.trim().isEmpty()) {
            result.addError("Age is required");
        } else {
            try {
                int ageInt = Integer.parseInt(age.trim());
                if (ageInt < 13) {
                    result.addError("Age must be at least 13 years");
                } else if (ageInt > 120) {
                    result.addError("Age must be less than 120 years");
                }
            } catch (NumberFormatException e) {
                result.addError("Age must be a valid number");
            }
        }

        // Weight validation
        if (weight == null || weight.trim().isEmpty()) {
            result.addError("Weight is required");
        } else {
            try {
                float weightFloat = Float.parseFloat(weight.trim());
                if (weightFloat < 20) {
                    result.addError("Weight must be at least 20 kg");
                } else if (weightFloat > 500) {
                    result.addError("Weight must be less than 500 kg");
                }
            } catch (NumberFormatException e) {
                result.addError("Weight must be a valid number");
            }
        }

        // Height validation
        if (height == null || height.trim().isEmpty()) {
            result.addError("Height is required");
        } else {
            try {
                float heightFloat = Float.parseFloat(height.trim());
                if (heightFloat < 100) {
                    result.addError("Height must be at least 100 cm");
                } else if (heightFloat > 300) {
                    result.addError("Height must be less than 300 cm");
                }
            } catch (NumberFormatException e) {
                result.addError("Height must be a valid number");
            }
        }

        return result;
    }

    // Exercise validation
    public static ValidationResult validateExercise(String exerciseName, String duration, String caloriesBurnt) {
        ValidationResult result = new ValidationResult();

        // Exercise name validation
        if (exerciseName == null || exerciseName.trim().isEmpty()) {
            result.addError("Exercise name is required");
        } else if (exerciseName.trim().length() < 2) {
            result.addError("Exercise name must be at least 2 characters");
        } else if (exerciseName.trim().length() > 50) {
            result.addError("Exercise name must be less than 50 characters");
        }

        // Duration validation
        if (duration == null || duration.trim().isEmpty()) {
            result.addError("Duration is required");
        } else {
            try {
                int durationInt = Integer.parseInt(duration.trim());
                if (durationInt < 1) {
                    result.addError("Duration must be at least 1 minute");
                } else if (durationInt > 600) { // 10 hours max
                    result.addError("Duration must be less than 600 minutes");
                }
            } catch (NumberFormatException e) {
                result.addError("Duration must be a valid number");
            }
        }

        // Calories burnt validation
        if (caloriesBurnt == null || caloriesBurnt.trim().isEmpty()) {
            result.addError("Calories burnt is required");
        } else {
            try {
                int caloriesInt = Integer.parseInt(caloriesBurnt.trim());
                if (caloriesInt < 1) {
                    result.addError("Calories burnt must be at least 1");
                } else if (caloriesInt > 5000) {
                    result.addError("Calories burnt must be less than 5000");
                }
            } catch (NumberFormatException e) {
                result.addError("Calories burnt must be a valid number");
            }
        }

        return result;
    }

    // Meal validation
    public static ValidationResult validateMeal(String mealName, String caloriesConsumed, String waterIntake) {
        ValidationResult result = new ValidationResult();

        // Meal name validation
        if (mealName == null || mealName.trim().isEmpty()) {
            result.addError("Meal name is required");
        } else if (mealName.trim().length() < 2) {
            result.addError("Meal name must be at least 2 characters");
        } else if (mealName.trim().length() > 100) {
            result.addError("Meal name must be less than 100 characters");
        }

        // Calories consumed validation
        if (caloriesConsumed == null || caloriesConsumed.trim().isEmpty()) {
            result.addError("Calories consumed is required");
        } else {
            try {
                int caloriesInt = Integer.parseInt(caloriesConsumed.trim());
                if (caloriesInt < 1) {
                    result.addError("Calories consumed must be at least 1");
                } else if (caloriesInt > 10000) {
                    result.addError("Calories consumed must be less than 10000");
                }
            } catch (NumberFormatException e) {
                result.addError("Calories consumed must be a valid number");
            }
        }

        // Water intake validation
        if (waterIntake == null || waterIntake.trim().isEmpty()) {
            result.addError("Water intake is required");
        } else {
            try {
                float waterFloat = Float.parseFloat(waterIntake.trim());
                if (waterFloat < 0.1f) {
                    result.addError("Water intake must be at least 0.1 liters");
                } else if (waterFloat > 20.0f) {
                    result.addError("Water intake must be less than 20 liters");
                }
            } catch (NumberFormatException e) {
                result.addError("Water intake must be a valid number");
            }
        }

        return result;
    }

    // Email validation
    public static ValidationResult validateEmail(String email) {
        ValidationResult result = new ValidationResult();

        if (email == null || email.trim().isEmpty()) {
            result.addError("Email is required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            result.addError("Please enter a valid email address");
        } else if (email.trim().length() > 100) {
            result.addError("Email must be less than 100 characters");
        }

        return result;
    }

    // Password validation
    public static ValidationResult validatePassword(String password) {
        ValidationResult result = new ValidationResult();

        if (password == null || password.isEmpty()) {
            result.addError("Password is required");
        } else if (password.length() < 6) {
            result.addError("Password must be at least 6 characters");
        } else if (password.length() > 128) {
            result.addError("Password must be less than 128 characters");
        }

        // Optional: Add complexity requirements
        if (password != null && password.length() >= 6) {
            boolean hasLetter = password.matches(".*[a-zA-Z].*");
            boolean hasDigit = password.matches(".*\\d.*");

            if (!hasLetter) {
                result.addError("Password should contain at least one letter");
            }
            if (!hasDigit) {
                result.addError("Password should contain at least one number");
            }
        }

        return result;
    }

    // BMI calculation helper
    public static String calculateBMI(String weight, String height) {
        try {
            float weightKg = Float.parseFloat(weight);
            float heightCm = Float.parseFloat(height);
            float heightM = heightCm / 100; // Convert cm to meters

            float bmi = weightKg / (heightM * heightM);

            String category;
            if (bmi < 18.5) {
                category = "Underweight";
            } else if (bmi < 25) {
                category = "Normal weight";
            } else if (bmi < 30) {
                category = "Overweight";
            } else {
                category = "Obese";
            }

            return String.format("BMI: %.1f (%s)", bmi, category);
        } catch (NumberFormatException e) {
            return "BMI: Unable to calculate";
        }
    }

    // Recommended daily calories calculation
    public static int calculateRecommendedCalories(String age, String weight, String height, String gender) {
        try {
            int ageInt = Integer.parseInt(age);
            float weightKg = Float.parseFloat(weight);
            float heightCm = Float.parseFloat(height);

            // Using Mifflin-St Jeor Equation
            float bmr;
            if (gender != null && gender.toLowerCase().equals("female")) {
                bmr = (10 * weightKg) + (6.25f * heightCm) - (5 * ageInt) - 161;
            } else {
                // Default to male calculation
                bmr = (10 * weightKg) + (6.25f * heightCm) - (5 * ageInt) + 5;
            }

            // Multiply by activity factor (assuming lightly active)
            return Math.round(bmr * 1.375f);
        } catch (NumberFormatException e) {
            return 2000; // Default recommended calories
        }
    }

    // Water intake recommendation
    public static float calculateRecommendedWater(String weight) {
        try {
            float weightKg = Float.parseFloat(weight);
            // 35ml per kg of body weight
            return (weightKg * 35) / 1000; // Convert ml to liters
        } catch (NumberFormatException e) {
            return 2.0f; // Default 2 liters
        }
    }

    // Health insights based on data
    public static String generateHealthInsight(String weight, String height, int totalCaloriesBurned,
                                               int totalCaloriesConsumed, float totalWaterIntake) {
        StringBuilder insight = new StringBuilder();

        // BMI insight
        String bmi = calculateBMI(weight, height);
        insight.append("📊 ").append(bmi).append("\n\n");

        // Calorie balance insight
        int netCalories = totalCaloriesConsumed - totalCaloriesBurned;
        if (netCalories > 500) {
            insight.append("⚠️ High calorie surplus today. Consider increasing physical activity.\n\n");
        } else if (netCalories < -500) {
            insight.append("⚠️ High calorie deficit today. Ensure adequate nutrition.\n\n");
        } else if (netCalories > 0 && netCalories <= 500) {
            insight.append("✅ Moderate calorie surplus - good for muscle building.\n\n");
        } else {
            insight.append("✅ Balanced calorie intake and expenditure.\n\n");
        }

        // Water intake insight
        float recommendedWater = calculateRecommendedWater(weight);
        if (totalWaterIntake < recommendedWater * 0.8f) {
            insight.append("💧 Consider increasing water intake. Target: ")
                    .append(String.format("%.1fL", recommendedWater)).append("\n\n");
        } else if (totalWaterIntake >= recommendedWater) {
            insight.append("💧 Great hydration levels! Keep it up.\n\n");
        }

        return insight.toString();
    }
}