package com.example.dreamjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonPressed(View v) {
        int idNumber = v.getId();
        String idName = getResources().getResourceEntryName(idNumber);

        Intent i;

        switch (idName) {
            case "btnDreamListActivity":
                i = new Intent(this, DreamListActivity.class);
                startActivity(i);
                break;
            case "btnSymbolListActivity":
                i = new Intent(this, SymbolListActivity.class);
                startActivity(i);
                break;
        }
    }

}