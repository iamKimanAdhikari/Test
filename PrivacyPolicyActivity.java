package com.example.healthtrackerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView tvPrivacyPolicy;
    private Button btnBack;
    private SecureStorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageManager = new SecureStorageManager(this);
        applyTheme();
        setContentView(R.layout.activity_privacy_policy);

        initializeViews();
        setupPrivacyPolicyContent();
        setupClickListeners();
    }

    private void initializeViews() {
        tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupPrivacyPolicyContent() {
        String privacyPolicyText =
                "HEALTH TRACKER APP - PRIVACY POLICY\n\n" +

                        "📅 Last Updated: " + java.text.DateFormat.getDateInstance().format(new java.util.Date()) + "\n\n" +

                        "🛡️ PRIVACY-FIRST APPROACH\n" +
                        "Your health data is extremely sensitive, and we treat it with the highest level of security and respect. " +
                        "This app is designed to comply with both the European Union's General Data Protection Regulation (GDPR) " +
                        "and Australia's Privacy Principles (APPs).\n\n" +

                        "📊 WHAT DATA WE COLLECT\n" +
                        "Essential Health Data:\n" +
                        "• Personal profile (name, age, weight, height)\n" +
                        "• Exercise activities and duration\n" +
                        "• Calorie intake and burn data\n" +
                        "• Water consumption records\n" +
                        "• Login credentials (encrypted)\n\n" +

                        "Optional Data (with your consent):\n" +
                        "• App usage analytics (anonymous)\n" +
                        "• Device performance data\n" +
                        "• Marketing preferences\n\n" +

                        "🔒 HOW WE PROTECT YOUR DATA\n" +
                        "LOCAL STORAGE ONLY:\n" +
                        "• All health data remains on YOUR device\n" +
                        "• No data is transmitted to external servers\n" +
                        "• No cloud storage or remote backups\n\n" +

                        "ENCRYPTION:\n" +
                        "• Sensitive data encrypted using AES-256-GCM\n" +
                        "• Passwords hashed with secure algorithms\n" +
                        "• Master keys generated using Android Keystore\n\n" +

                        "🌍 GDPR COMPLIANCE (EU)\n" +
                        "Your Rights Under GDPR:\n" +
                        "• RIGHT TO ACCESS: View all your stored data via 'Export Data'\n" +
                        "• RIGHT TO RECTIFICATION: Edit your profile and health data anytime\n" +
                        "• RIGHT TO ERASURE: Delete all data via 'Delete All Data'\n" +
                        "• RIGHT TO PORTABILITY: Export your data in readable format\n" +
                        "• RIGHT TO OBJECT: Opt-out of analytics and marketing\n" +
                        "• RIGHT TO WITHDRAW CONSENT: Change privacy settings anytime\n\n" +

                        "LAWFUL BASIS FOR PROCESSING:\n" +
                        "• Essential health tracking: Legitimate interest\n" +
                        "• Analytics and marketing: Your explicit consent\n\n" +

                        "🇦🇺 AUSTRALIAN PRIVACY PRINCIPLES (APPs)\n" +
                        "APP 1 - Open and Transparent Management:\n" +
                        "• This policy clearly explains our data practices\n" +
                        "• Privacy officer contact available in settings\n\n" +

                        "APP 3 - Collection of Solicited Information:\n" +
                        "• We only collect health data necessary for app functionality\n" +
                        "• Optional data requires explicit consent\n\n" +

                        "APP 5 - Notification of Collection:\n" +
                        "• You're informed of all data collection at registration\n" +
                        "• Consent screen explains each data type\n\n" +

                        "APP 6 - Use or Disclosure:\n" +
                        "• Health data used only for tracking purposes\n" +
                        "• No disclosure to third parties\n" +
                        "• Analytics data anonymized\n\n" +

                        "APP 11 - Security of Personal Information:\n" +
                        "• Military-grade AES-256 encryption\n" +
                        "• Regular security audits and updates\n" +
                        "• No data transmission reduces breach risk\n\n" +

                        "APP 12 - Access to Personal Information:\n" +
                        "• Full data export functionality\n" +
                        "• Real-time access to all stored information\n\n" +

                        "APP 13 - Correction of Personal Information:\n" +
                        "• Edit profile and health data anytime\n" +
                        "• Immediate updates and corrections\n\n" +

                        "🚫 WHAT WE DON'T DO\n" +
                        "• NO data selling or sharing\n" +
                        "• NO advertising networks\n" +
                        "• NO location tracking\n" +
                        "• NO social media integration\n" +
                        "• NO cloud storage without consent\n" +
                        "• NO behavioral profiling\n\n" +

                        "👶 CHILDREN'S PRIVACY\n" +
                        "This app is not intended for users under 13. If you're under 18, " +
                        "please get parental consent before using this app.\n\n" +

                        "🔄 DATA RETENTION\n" +
                        "• Health data retained until you delete it\n" +
                        "• Analytics data anonymized after 90 days\n" +
                        "• Account data deleted immediately upon request\n\n" +

                        "📧 CONTACT US\n" +
                        "Privacy Officer: privacy@healthtracker.app\n" +
                        "Data Protection Officer: dpo@healthtracker.app\n" +
                        "General Inquiries: support@healthtracker.app\n\n" +

                        "🔄 POLICY UPDATES\n" +
                        "We'll notify you of any material changes to this policy. " +
                        "Continued use after updates constitutes acceptance.\n\n" +

                        "⚖️ REGULATORY INFORMATION\n" +
                        "EU Representative: [Your EU Representative]\n" +
                        "Australian Privacy Commissioner: www.oaic.gov.au\n" +
                        "ICO (UK): ico.org.uk\n\n" +

                        "This policy demonstrates our commitment to protecting your privacy " +
                        "while providing you with powerful health tracking capabilities.";

        tvPrivacyPolicy.setText(privacyPolicyText);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
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