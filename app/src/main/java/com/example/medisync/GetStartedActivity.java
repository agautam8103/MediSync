package com.example.medisync;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    private static final String PREF_FIRST_TIME = "first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the app is opened for the first time
        SharedPreferences preferences = getSharedPreferences("com.example.medisync", MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(PREF_FIRST_TIME, true);

        if (isFirstTime) {
            // If first time, launch GetStartedActivity
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_FIRST_TIME, false);
            editor.apply();
            launchGetStarted();
        } else {
            // If not first time, launch Dashboard directly
            launchDashboard();
        }
    }

    private void launchGetStarted() {
        setContentView(R.layout.activity_main);
        Button get_started_btn = findViewById(R.id.new_usr_get_started_btn);
        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
        finish(); // Finish this activity so it's not on the back stack
    }

    private void launchDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish(); // Finish this activity so it's not on the back stack
    }
}
