package com.example.medisync;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MedMinder extends AppCompatActivity {

    ListView medlist;
    Button medlog, reminderbtn;
    ArrayAdapter<String> adapter;
    ArrayList<String> medications;

    // SharedPreferences
    SharedPreferences sharedPreferences;
    private static final String MEDICATIONS_KEY = "medications";
    private static final String REMINDER_TIME_KEY = "reminder_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medminder);

        medlist = findViewById(R.id.medicationListView);
        medlog = findViewById(R.id.medicationLogButton);
        reminderbtn = findViewById(R.id.reminderSettingsButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MedSyncPrefs", Context.MODE_PRIVATE);

        // Retrieve medications from SharedPreferences or use default list
        medications = new ArrayList<>(sharedPreferences.getStringSet(MEDICATIONS_KEY, new HashSet<String>()));

        // Initialize adapter with medications list
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medications);
        medlist.setAdapter(adapter);

        medlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        reminderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReminderDialog();
            }
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addmed);
        dialog.show();

        // Find EditText and Button inside the dialog
        EditText medNameEditText = dialog.findViewById(R.id.txt_input);
        Button addMedButton = dialog.findViewById(R.id.btn_okay);

        addMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered by the user
                String newMedName = medNameEditText.getText().toString();

                // Add the new medication to the medications list
                medications.add(newMedName);

                // Update SharedPreferences with the new medications list
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(MEDICATIONS_KEY, new HashSet<>(medications));
                editor.apply();

                // Notify the adapter of the change
                adapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });
    }

    private void showReminderDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.reminder_dialog);
        dialog.show();

        TimePicker timePicker = dialog.findViewById(R.id.time_picker);
        Button setReminderButton = dialog.findViewById(R.id.set_reminder_button);

        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Save reminder time to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(REMINDER_TIME_KEY + "_hour", hour);
                editor.putInt(REMINDER_TIME_KEY + "_minute", minute);
                editor.apply();

                // Set alarm for the selected time
                setAlarm(hour, minute);

                Toast.makeText(MedMinder.this, "Reminder set for " + hour + ":" + minute, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }

    private void setAlarm(int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); // Specify FLAG_IMMUTABLE

        // Set the alarm to start at the specified time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Schedule the alarm to repeat every few hours (you can adjust this as needed)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR * 3, pendingIntent);
    }

}
