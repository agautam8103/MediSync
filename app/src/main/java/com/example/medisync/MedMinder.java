package com.example.medisync;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class MedMinder extends AppCompatActivity {

    ListView medlist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medminder);

        medlist = findViewById(R.id.medicationListView);

    }


}
