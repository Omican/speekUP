package com.example.maickel.speechy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void openTextRecognition(View v){
        Intent intent = new Intent(getApplicationContext(), choosePresentationMode.class);
        startActivity(intent);
    }

    public void openOptionScreen(View v){
        Intent intent = new Intent(getApplicationContext(), Options.class);
        startActivity(intent);
    }

    public void openAnalysticsScreen(View v){
        Intent intent = new Intent(getApplicationContext(), Analytics.class);
        startActivity(intent);
    }

    public void openRecordingsScreen(View v){
        Intent intent = new Intent(getApplicationContext(), Recordings.class);
        startActivity(intent);
    }
}
