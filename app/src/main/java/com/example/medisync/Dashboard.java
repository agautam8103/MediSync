package com.example.medisync;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {


    private Database medi_user_saved_data;
    TextView greeting_oxy_user, dash_mesg, heart_rate_label, blood_oxygen_level_label, body_temperature_label,
            Click_to_see_more_label1, Click_to_see_more_label2, Click_to_see_more_label3;
    Button pill_dispenser, mdalert, medminder;
    ImageView heart_pic, temp_pic, blood_oxy_pic, logo;
    private Toolbar toolbar;

    private MediUser Medi_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        medi_user_saved_data = new Database(Dashboard.this);
        greeting_oxy_user = findViewById(R.id.welcomemessage);
        medi_user_saved_data = new Database(Dashboard.this);

        pill_dispenser = findViewById(R.id.pilldispenserbutton);
        pill_dispenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pillDispenser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        MediUser medi_user = medi_user_saved_data.getSavedOxyUser();

        if (medi_user.MediUserIsEmpty()) { //Go to register user page if there's no user data
            Intent i = new Intent(this, GetStartedActivity.class);
            startActivity(i);
        } else { //Set everything visible on this page if a user account exists
            greeting_oxy_user.setText("Hello, " + medi_user.getFirstName() + "!");
        }


    }

    private void pillDispenser() {
        Intent intent = new Intent(this, PillDispenser.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() { //Stay in this page if back phone button is pressed at any page
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }




}

