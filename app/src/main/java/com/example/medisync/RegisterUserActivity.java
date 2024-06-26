package com.example.medisync;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            // Add other permissions as needed
    };
    private static final int PERMISSION_REQUEST_CODE = 123;

    private EditText first_name, last_name, birth_year, height, weight;
    private String sex, height_unit_c, weight_unit_c;
    private Button next;
    private Database save_medi_user;
    private Spinner sex_choice, height_units, weight_units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        save_medi_user = new Database(RegisterUserActivity.this);
        MediUser user = save_medi_user.getSavedOxyUser();

        next = findViewById(R.id.save_usr_data_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOxyUser();
            }
        });

        first_name = findViewById(R.id.f_name_input);
        last_name = findViewById(R.id.l_name_input);
        birth_year = findViewById(R.id.birth_year_input);
        height = findViewById(R.id.height_input);
        weight = findViewById(R.id.weight_input);

        sex_choice = findViewById(R.id.sex_input);
        ArrayAdapter<CharSequence> sex_adapter = ArrayAdapter.createFromResource(this, R.array.Sex_Options, android.R.layout.simple_spinner_item);
        sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex_choice.setAdapter(sex_adapter);
        sex_choice.setSelection(getIndexOfSelectedValue(sex_choice, user.getSex()));
        sex_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        height_units = findViewById(R.id.height_units);
        ArrayAdapter<CharSequence> height_units_adapter = ArrayAdapter.createFromResource(this, R.array.Height_Units, android.R.layout.simple_spinner_item);
        height_units_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        height_units.setAdapter(height_units_adapter);
        height_units.setSelection(getIndexOfSelectedValue(height_units, user.getHeightUnit()));
        height_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                height_unit_c = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        weight_units = findViewById(R.id.weight_units);
        ArrayAdapter<CharSequence> weight_units_adapter = ArrayAdapter.createFromResource(this, R.array.Weight_Units, android.R.layout.simple_spinner_item);
        weight_units_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weight_units.setAdapter(weight_units_adapter);
        weight_units.setSelection(getIndexOfSelectedValue(weight_units, user.getWeightUnit()));
        weight_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weight_unit_c = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        MediUser user = save_medi_user.getSavedOxyUser();

        if (user.MediUserIsEmpty()) EnableEdit(true);

        else {
            EnableEdit(false);

            first_name.setText(user.getFirstName());
            last_name.setText(user.getLastName());
            birth_year.setText(Integer.toString(user.getBirthYear()));
            height.setText(Float.toString(user.getHeight()));
            weight.setText(Float.toString(user.getWeight()));
        }
    }

    private void saveOxyUser() {
        if (first_name.getText().length() == 0 || last_name.getText().length() == 0 || birth_year.getText().length() == 0 ||
                height.getText().length() == 0 || weight.getText().length() == 0) {
            Toast empty_message = Toast.makeText(getApplicationContext(), "No Field May Be Empty", Toast.LENGTH_LONG);
            empty_message.show();
        } else if (Integer.parseInt(birth_year.getText().toString()) < 1900 || Integer.parseInt(birth_year.getText().toString()) > 2023) {
            Toast invalid_birth_year_message = Toast.makeText(getApplicationContext(), "Enter birth year between 1900 and 2023", Toast.LENGTH_LONG);
            invalid_birth_year_message.show();
        } else if ((2023 - Integer.parseInt(birth_year.getText().toString())) < 18) {
            Toast under_age_message = Toast.makeText(getApplicationContext(), "You are under 18 years old. OxyRhythm is not for you", Toast.LENGTH_LONG);
            under_age_message.show();
        } else {
            save_medi_user.saveFirstName(first_name.getText().toString());
            save_medi_user.saveLastName(last_name.getText().toString());
            save_medi_user.saveBirthYear(Integer.parseInt(birth_year.getText().toString()));
            save_medi_user.saveWeight(Float.parseFloat(weight.getText().toString()));
            save_medi_user.saveHeight(Float.parseFloat(height.getText().toString()));
            save_medi_user.saveSex(sex);
            save_medi_user.saveHeightUnit(height_unit_c);
            save_medi_user.saveWeightUnit(weight_unit_c);

            Intent i = new Intent(this, Dashboard.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }





    private int getIndexOfSelectedValue(Spinner spinner, String value) {
        SpinnerAdapter adapter = spinner.getAdapter();

        if (adapter instanceof ArrayAdapter) {
            ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>) adapter;

            for (int i = 0; i < arrayAdapter.getCount(); i++) {
                if (arrayAdapter.getItem(i).equals(value)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void EnableEdit(boolean enable) {
        first_name.setEnabled(enable);
        last_name.setEnabled(enable);
        birth_year.setEnabled(enable);
        height.setEnabled(enable);
        weight.setEnabled(enable);
        sex_choice.setEnabled(enable);
        height_units.setEnabled(enable);
        weight_units.setEnabled(enable);

        if (enable) {
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
        }
    }
}