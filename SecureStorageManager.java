package com.example.healthtrackerapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SecureStorageManager {
    private static final String ENCRYPTED_PREFS_FILE = "encrypted_health_prefs";
    private SharedPreferences encryptedPrefs;
    private SharedPreferences regularPrefs;

    public SecureStorageManager(Context context) {
        try {
            // Create master key for encryption
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Create encrypted shared preferences
            encryptedPrefs = EncryptedSharedPreferences.create(
                    context,
                    ENCRYPTED_PREFS_FILE,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // Keep regular preferences for non-sensitive data like theme settings
            regularPrefs = context.getSharedPreferences("HealthTrackerPrefs", Context.MODE_PRIVATE);

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to initialize secure storage", e);
        }
    }

    // Secure storage methods for sensitive data
    public void putSecureString(String key, String value) {
        encryptedPrefs.edit().putString(key, value).apply();
    }

    public String getSecureString(String key, String defaultValue) {
        return encryptedPrefs.getString(key, defaultValue);
    }

    public void putSecureBoolean(String key, boolean value) {
        encryptedPrefs.edit().putBoolean(key, value).apply();
    }

    public boolean getSecureBoolean(String key, boolean defaultValue) {
        return encryptedPrefs.getBoolean(key, defaultValue);
    }

    public void removeSecureData(String key) {
        encryptedPrefs.edit().remove(key).apply();
    }

    // Regular storage methods for non-sensitive data
    public void putString(String key, String value) {
        regularPrefs.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return regularPrefs.getString(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        regularPrefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return regularPrefs.getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        regularPrefs.edit().remove(key).apply();
    }

    // Clear all data
    public void clearAllData() {
        encryptedPrefs.edit().clear().apply();
        regularPrefs.edit().clear().apply();
    }
}