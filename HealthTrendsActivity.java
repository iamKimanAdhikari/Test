package com.example.healthtrackerapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HealthTrendsActivity extends AppCompatActivity {
    private LineChart caloriesTrendChart;
    private BarChart exerciseBarChart;
    private PieChart waterIntakePieChart;
    private Button btnPrevious;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_health_trends);

        initializeViews();
        setupCharts();
        loadChartData();
        setupClickListeners();
    }

    private void initializeViews() {
        caloriesTrendChart = findViewById(R.id.caloriesTrendChart);
        exerciseBarChart = findViewById(R.id.exerciseBarChart);
        waterIntakePieChart = findViewById(R.id.waterIntakePieChart);
        btnPrevious = findViewById(R.id.btnPrevious);
    }

    private void setupCharts() {
        setupCaloriesTrendChart();
        setupExerciseBarChart();
        setupWaterIntakePieChart();
    }

    private void setupCaloriesTrendChart() {
        caloriesTrendChart.setTouchEnabled(true);
        caloriesTrendChart.setDragEnabled(true);
        caloriesTrendChart.setScaleEnabled(true);
        caloriesTrendChart.setPinchZoom(true);

        Description desc = new Description();
        desc.setText("Calories Burned vs Consumed Over Time");
        desc.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        caloriesTrendChart.setDescription(desc);

        XAxis xAxis = caloriesTrendChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = caloriesTrendChart.getAxisLeft();
        leftAxis.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);

        YAxis rightAxis = caloriesTrendChart.getAxisRight();
        rightAxis.setEnabled(false);

        caloriesTrendChart.getLegend().setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
    }

    private void setupExerciseBarChart() {
        exerciseBarChart.setTouchEnabled(true);
        exerciseBarChart.setDragEnabled(true);
        exerciseBarChart.setScaleEnabled(true);

        Description desc = new Description();
        desc.setText("Exercise Duration by Type");
        desc.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        exerciseBarChart.setDescription(desc);

        XAxis xAxis = exerciseBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = exerciseBarChart.getAxisLeft();
        leftAxis.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = exerciseBarChart.getAxisRight();
        rightAxis.setEnabled(false);

        exerciseBarChart.getLegend().setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
    }

    private void setupWaterIntakePieChart() {
        waterIntakePieChart.setUsePercentValues(true);
        waterIntakePieChart.setDrawHoleEnabled(true);
        waterIntakePieChart.setHoleColor(Color.TRANSPARENT);
        waterIntakePieChart.setTransparentCircleRadius(61f);

        Description desc = new Description();
        desc.setText("Water Intake Distribution");
        desc.setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        waterIntakePieChart.setDescription(desc);

        waterIntakePieChart.setDrawCenterText(true);
        waterIntakePieChart.setCenterText("Water\nIntake");
        waterIntakePieChart.setCenterTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);

        waterIntakePieChart.getLegend().setTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
    }

    private void loadChartData() {
        loadCaloriesData();
        loadExerciseData();
        loadWaterIntakeData();
    }

    private void loadCaloriesData() {
        String exerciseHistory = storageManager.getString("exerciseHistory", "");
        String mealHistory = storageManager.getString("mealHistory", "");

        Map<String, Float> caloriesBurned = parseCaloriesFromHistory(exerciseHistory, true);
        Map<String, Float> caloriesConsumed = parseCaloriesFromHistory(mealHistory, false);

        List<Entry> burnedEntries = new ArrayList<>();
        List<Entry> consumedEntries = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        // Combine and sort dates
        Set<String> allDates = new HashSet<>();
        allDates.addAll(caloriesBurned.keySet());
        allDates.addAll(caloriesConsumed.keySet());

        List<String> sortedDates = new ArrayList<>(allDates);
        Collections.sort(sortedDates);

        for (int i = 0; i < sortedDates.size() && i < 10; i++) { // Show last 10 entries
            String date = sortedDates.get(i);
            dates.add(date.substring(5, 10)); // MM-dd format

            burnedEntries.add(new Entry(i, caloriesBurned.getOrDefault(date, 0f)));
            consumedEntries.add(new Entry(i, caloriesConsumed.getOrDefault(date, 0f)));
        }

        LineDataSet burnedDataSet = new LineDataSet(burnedEntries, "Calories Burned");
        burnedDataSet.setColor(Color.RED);
        burnedDataSet.setCircleColor(Color.RED);
        burnedDataSet.setLineWidth(2f);
        burnedDataSet.setCircleRadius(4f);

        LineDataSet consumedDataSet = new LineDataSet(consumedEntries, "Calories Consumed");
        consumedDataSet.setColor(Color.GREEN);
        consumedDataSet.setCircleColor(Color.GREEN);
        consumedDataSet.setLineWidth(2f);
        consumedDataSet.setCircleRadius(4f);

        LineData lineData = new LineData(burnedDataSet, consumedDataSet);
        caloriesTrendChart.setData(lineData);

        XAxis xAxis = caloriesTrendChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setGranularity(1f);

        caloriesTrendChart.invalidate();
    }

    private void loadExerciseData() {
        String exerciseHistory = storageManager.getString("exerciseHistory", "");
        Map<String, Float> exerciseTypes = new HashMap<>();

        if (!exerciseHistory.isEmpty()) {
            String[] entries = exerciseHistory.split("\n");
            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(" \\| ");
                    if (parts.length >= 3) {
                        String exerciseName = parts[1];
                        try {
                            float duration = Float.parseFloat(parts[2].replaceAll("[^0-9.]", ""));
                            exerciseTypes.put(exerciseName,
                                    exerciseTypes.getOrDefault(exerciseName, 0f) + duration);
                        } catch (NumberFormatException e) {
                            // Skip invalid entries
                        }
                    }
                }
            }
        }

        List<BarEntry> barEntries = new ArrayList<>();
        List<String> exerciseNames = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Float> entry : exerciseTypes.entrySet()) {
            barEntries.add(new BarEntry(index, entry.getValue()));
            exerciseNames.add(entry.getKey());
            index++;
        }

        if (barEntries.isEmpty()) {
            // Add dummy data if no exercise data exists
            barEntries.add(new BarEntry(0, 0f));
            exerciseNames.add("No Data");
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Exercise Duration (minutes)");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        exerciseBarChart.setData(barData);

        XAxis xAxis = exerciseBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(exerciseNames));
        xAxis.setGranularity(1f);

        exerciseBarChart.invalidate();
    }

    private void loadWaterIntakeData() {
        String mealHistory = storageManager.getString("mealHistory", "");
        float totalWater = 0f;
        int daysWithData = 0;

        if (!mealHistory.isEmpty()) {
            String[] entries = mealHistory.split("\n");
            Set<String> uniqueDays = new HashSet<>();

            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(" \\| ");
                    if (parts.length >= 4) {
                        String date = parts[0].substring(0, 10); // Extract date part
                        uniqueDays.add(date);

                        try {
                            float water = Float.parseFloat(parts[3].replaceAll("[^0-9.]", ""));
                            totalWater += water;
                        } catch (NumberFormatException e) {
                            // Skip invalid entries
                        }
                    }
                }
            }
            daysWithData = uniqueDays.size();
        }

        List<PieEntry> pieEntries = new ArrayList<>();

        if (daysWithData > 0) {
            float averageWater = totalWater / daysWithData;
            float recommendedDaily = 2.0f; // 2 liters recommended

            if (averageWater >= recommendedDaily) {
                pieEntries.add(new PieEntry(recommendedDaily, "Recommended (2L)"));
                pieEntries.add(new PieEntry(averageWater - recommendedDaily, "Above Recommended"));
            } else {
                pieEntries.add(new PieEntry(averageWater, "Current Intake"));
                pieEntries.add(new PieEntry(recommendedDaily - averageWater, "Below Recommended"));
            }
        } else {
            // Dummy data when no water intake data exists
            pieEntries.add(new PieEntry(100f, "No Data Available"));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Water Intake");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(isDarkMode() ? Color.WHITE : Color.BLACK);
        pieDataSet.setValueTextSize(12f);

        PieData pieData = new PieData(pieDataSet);
        waterIntakePieChart.setData(pieData);
        waterIntakePieChart.invalidate();
    }

    private Map<String, Float> parseCaloriesFromHistory(String history, boolean isExercise) {
        Map<String, Float> caloriesMap = new HashMap<>();

        if (!history.isEmpty()) {
            String[] entries = history.split("\n");
            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.split(" \\| ");
                    if (parts.length >= 3) {
                        String date = parts[0].substring(0, 10); // Extract date part (yyyy-MM-dd)
                        try {
                            int calorieIndex = isExercise ? 3 : 2; // Different index for exercise vs meal
                            if (parts.length > calorieIndex) {
                                float calories = Float.parseFloat(parts[calorieIndex].replaceAll("[^0-9.]", ""));
                                caloriesMap.put(date, caloriesMap.getOrDefault(date, 0f) + calories);
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid entries
                        }
                    }
                }
            }
        }

        return caloriesMap;
    }

    private void setupClickListeners() {
        btnPrevious.setOnClickListener(v -> finish());
    }

    private boolean isDarkMode() {
        return storageManager.getBoolean("isDarkMode", false);
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