package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantryPreferences";
    private static final String KEY_EXPIRY_ALERTS = "expiryAlerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch expirySwitch =
                findViewById(R.id.expiryAlertsSwitch);

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        expirySwitch.setChecked(
                preferences.getBoolean(KEY_EXPIRY_ALERTS, true)
        );

        expirySwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) ->
                        preferences.edit()
                                .putBoolean(KEY_EXPIRY_ALERTS, isChecked)
                                .apply()
        );

        findViewById(R.id.settingsBackButton)
                .setOnClickListener(v -> finish());
    }
}