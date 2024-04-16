package com.example.medisync;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

public class Dashboard extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_NOTIFICATION = 1001;
    private static final int PERMISSION_REQUEST_ALARM = 1002;
    private Database medi_user_saved_data;
    private TextView greeting_oxy_user;
    private MediUser Medi_user;
    private Button pillDispenser, mdAlert, medMinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        medi_user_saved_data = new Database(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.welcomemessage);
        pillDispenser = findViewById(R.id.pilldispenserbutton);
        mdAlert = findViewById(R.id.mdalert);
        medMinder = findViewById(R.id.medminder);

        pillDispenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pillDispenser();
            }
        });

        mdAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdAlert();
            }
        });

        medMinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medMinder();
            }
        });

        checkPermissions();
        checkUser();
    }

    private void checkUser() {
        MediUser medi_user = medi_user_saved_data.getSavedOxyUser();

        if (medi_user.MediUserIsEmpty()) {
            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        } else {
            greeting_oxy_user.setText("Hello " + medi_user.getFirstName() + "!");
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY, Manifest.permission.SET_ALARM}, PERMISSION_REQUEST_NOTIFICATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Notification permission granted
            } else {
                showPermissionDeniedSnackbar();
            }
        } else if (requestCode == PERMISSION_REQUEST_ALARM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Alarm permission granted
            } else {
                showPermissionDeniedSnackbar();
            }
        }
    }

    private void showPermissionDeniedSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "You have denied permission to access notifications or set alarms. These features may not work properly.", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle action
                    }
                })
                .show();
    }

    private void pillDispenser() {
        Intent intent = new Intent(this, PillDispenser.class);
        startActivity(intent);
    }

    private void medMinder() {
        Intent intent = new Intent(this, MedMinder.class);
        startActivity(intent);
    }

    private void mdAlert() {
        Intent intent = new Intent(this, MDAlert.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Handle back button press if needed
    }
}
